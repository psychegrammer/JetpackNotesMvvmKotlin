package com.wiseassblog.jetpacknotesmvvmkotlin.model.implementations

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wiseassblog.jetpacknotesmvvmkotlin.common.Result
import com.wiseassblog.jetpacknotesmvvmkotlin.common.awaitTaskCompletable
import com.wiseassblog.jetpacknotesmvvmkotlin.model.User
import com.wiseassblog.jetpacknotesmvvmkotlin.model.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseUserRepoImpl(val auth: FirebaseAuth = FirebaseAuth.getInstance()) : IUserRepository {

    override suspend fun signInGoogleUser(idToken: String):
            Result<Exception, Unit> = withContext(Dispatchers.IO) {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            awaitTaskCompletable(auth.signInWithCredential(credential))

            Result.build { Unit }
        } catch (e: Exception) {
            Result.build { throw e }
        }
    }


    override suspend fun signOutCurrentUser(): Result<Exception, Unit> {
        return Result.build {
            auth.signOut()
        }
    }

    override suspend fun getCurrentUser(): Result<Exception, User?> {
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            return Result.build { null }
        } else return Result.build {
            User(
                firebaseUser.uid,
                firebaseUser.displayName ?: ""
            )
        }
    }
}