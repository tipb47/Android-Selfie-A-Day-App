package com.example.project9

import android.hardware.Sensor
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AppViewModel : ViewModel() {
    private lateinit var accelerometerSensor: Sensor

    val auth = FirebaseAuth.getInstance()

    var user: User = User("","")
    var imageId: String = ""
    var image = MutableLiveData<Image>()

    private val _images: MutableLiveData<MutableList<Image>> = MutableLiveData()
    val images: LiveData<List<Image>>
        get() = _images as LiveData<List<Image>>

    val _navigateToImage = MutableLiveData<String?>()
    val navigateToImage: LiveData<String?>
        get() = _navigateToImage

    private val _errorHappened = MutableLiveData<String?>()
    val errorHappened: LiveData<String?>
        get() = _errorHappened

    private val _navigateToGallery = MutableLiveData<Boolean>(false)
    val navigateToGallery: LiveData<Boolean>
        get() = _navigateToGallery

    private val _navigateToSelfie = MutableLiveData<String?>()
    val navigateToSelfie: LiveData<String?>
        get() = _navigateToSelfie

    private val _navigateToSignUp = MutableLiveData<Boolean>(false)
    val navigateToSignUp: LiveData<Boolean>
        get() = _navigateToSignUp

    private val _navigateToSignIn = MutableLiveData<Boolean>(false)
    val navigateToSignIn: LiveData<Boolean>
        get() = _navigateToSignIn

    lateinit var imagesCollection: DatabaseReference

    init {

        if (imageId.trim() == "") {
            image.value = Image()
        }
        _images.value = mutableListOf<Image>()

    }

    fun saveImage() {
        imagesCollection.push().setValue(image.value)
        _navigateToGallery.value = true
    }


    fun initializeTheDatabase() {

        val database = Firebase.database
        imagesCollection = database
            .getReference("images")
            .child(auth.currentUser!!.uid)


        imagesCollection.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //keep track of changing images database
                var imagesList: ArrayList<Image> = ArrayList()
                for (imageSnapshot in dataSnapshot.children) {
                    Log.d("ViewModel", "Image Data: $image")
                    var image = imageSnapshot.getValue<Image>()
                    image?.imageId = imageSnapshot.key!!
                    imagesList.add(image!!)
                }
                _images.value = imagesList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        })

    }

    //get image from firebase given image id
    fun getImageById(imageId: String, callback: (Image?) -> Unit) {
        val imageReference = imagesCollection.child(imageId)

        imageReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val image = dataSnapshot.getValue(Image::class.java)
                callback(image)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // error
                callback(null)
            }
        })
    }

    fun deleteImage(imageId : String) {
        imagesCollection.child(imageId).removeValue()
        _navigateToGallery.value = true
    }

    fun backToGallery() {
        _navigateToGallery.value = true
    }

    fun onImageClicked(selectedImage: Image) {
        _navigateToImage.value = selectedImage.imageId
        imageId = selectedImage.imageId
        image.value = selectedImage
    }

    //when shook
    fun onShake() {
        _navigateToSelfie.value = ""
        imageId = ""
        image.value = Image()
    }

    fun onSelfieNavigated() {
        _navigateToSelfie.value = null
    }

    fun onImageNavigated() {
        _navigateToImage.value = null
    }

    fun onNavigatedToGallery() {
        _navigateToGallery.value = false
    }

    fun navigateToSignUp() {
        _navigateToSignUp.value = true
    }

    fun onNavigatedToSignUp() {
        _navigateToSignUp.value = false
    }

    fun navigateToSignIn() {
        _navigateToSignIn.value = true
    }

    fun onNavigatedToSignIn() {
        _navigateToSignIn.value = false
    }

    //sign in w/ email, password, throw error if incorrect login
    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                initializeTheDatabase()
                _navigateToGallery.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    //signup with email, password, ensure password match.
    fun signUp(email: String, password: String, verifyPassword: String) {
        //one of the fields is empty
        if (email.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            _errorHappened.value = "Email and password cannot be empty."
            return
        }

        //passwords do not match
        if (!password.equals(verifyPassword)) {
            Log.d("tag", "$password $verifyPassword")
            _errorHappened.value = "Password does not matched re-entered password"
            return
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                user = User(email,password)
                _navigateToSignIn.value = true
            } else {
                _errorHappened.value = it.exception?.message
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _navigateToSignIn.value = true
    }


    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }


}