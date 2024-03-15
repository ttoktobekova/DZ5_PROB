import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.dz5_proba.R
import com.example.dz5_proba.create.CreateTaskFragment
import com.example.dz5_proba.data.FireBaseAuthManager
import com.example.dz5_proba.data.TaskManager
import com.example.dz5_proba.databinding.FragmentGalleryBinding
import com.example.dz5_proba.ui.Gallery.Adapter.TaskAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val taskManager = TaskManager()
    private val authManager = FireBaseAuthManager()
    private val adapter = TaskAdapter(taskManager::updateTask)

    //для google  регистрации
    private val auth = FirebaseAuth.getInstance()
    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .build()
    }
    private val mGoogleSignInClient by lazy {
        GoogleSignIn.getClient(requireActivity(), gso)
    }
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Обработка ошибки
                Log.e("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (auth.currentUser != null) {
            updateUI()
        }
        init()
        addListeners()
    }

    private fun init() {
        binding.rvTask.adapter = adapter
    }

    private fun addListeners() {
        binding.btnAddRandom.setOnClickListener {
            findNavController().navigate(R.id.action_galleryFragment_to_createTaskFragment)
            setFragmentResultListener(CreateTaskFragment.TASK_RESULT_KEY) { _, _ ->
                updateUI()
            }
        }

        binding.btnSignIn.setOnClickListener {
            if (!validate()) return@setOnClickListener
            signIn(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }

        binding.btnSignUp.setOnClickListener {
            if (!validate()) return@setOnClickListener
            signUp(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
    }

    private fun updateUI() {
        taskManager.getAllTasks { tasks ->
            adapter.submitList(tasks)
        }
    }

    private fun validate(): Boolean {
        if (binding.etEmail.text.isNullOrEmpty()) {
            binding.etEmail.error = "Введите почту"
            return false
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            binding.etPassword.error = "Введите пароль"
            return false
        }
        return true
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener {
                Log.e("auth", "SignIn failed", it)
                Toast.makeText(
                    requireContext(),
                    "Ошибка входа: адрес электронной почты или пароль указаны неправильно",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener {
                updateUI()
            }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener {
                Log.e("auth", "SignUp failed", it.cause)
                Toast.makeText(
                    requireContext(),
                    "Ошибка регистрации: адрес электронной почты или пароль указаны неправильно",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener {
                authManager.userSignedUp()
                updateUI()
            }
    }

    private fun firebaseAuthWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnFailureListener {
                Log.e("auth", "Ошибка Google Sign-In", it.cause)
                Toast.makeText(
                    requireContext(),
                    "Ошибка входа: не удалось войти в систему с помощью Google",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnSuccessListener {
                updateUI()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
