package io.aethibo.fork.ui.profile.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import io.aethibo.data.utils.Resource
import io.aethibo.domain.Repository
import io.aethibo.domain.User
import io.aethibo.fork.R
import io.aethibo.fork.databinding.FragmentProfileBinding
import io.aethibo.fork.ui.auth.utils.snackBar
import io.aethibo.fork.ui.profile.adapter.RepositoriesAdapter
import io.aethibo.fork.ui.profile.viewmodel.ProfileViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModel()
    private val binding: FragmentProfileBinding by viewBinding()
    private val repositoryAdapter: RepositoriesAdapter by lazy { RepositoriesAdapter() }

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentUser()
        viewModel.getUsersRepositories(mapOf("sort" to "updated"))

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.userMetadataStatus.asLiveData()
                .observe(viewLifecycleOwner) { result: Resource<User> ->
                    when (result) {
                        is Resource.Loading -> binding.profileProgressBar.isVisible = true
                        is Resource.Success -> {
                            binding.profileProgressBar.isVisible = false
                            setupMetadataUi(result.data as User)
                        }
                        is Resource.Failure -> {
                            binding.profileProgressBar.isVisible = false
                            snackBar(result.message ?: "Unknown error occurred!")
                        }
                    }
                }

        viewModel.repositoryStatus.asLiveData()
                .observe(viewLifecycleOwner) { result: Resource<List<Repository>> ->
                    when (result) {
                        is Resource.Loading -> binding.profileProgressBar.isVisible = true
                        is Resource.Success -> {
                            binding.profileProgressBar.isVisible = false
                            setupRepositoriesAdapter(result.data as List<Repository>)
                        }
                        is Resource.Failure -> {
                            binding.profileProgressBar.isVisible = false
                            snackBar(result.message ?: "Unknown error occurred")
                        }
                    }
                }
    }

    private fun setupRepositoriesAdapter(repositories: List<Repository>) {
        repositoryAdapter.submitList(repositories)
        binding.rvProfileRepositories.adapter = repositoryAdapter
    }

    private fun setupMetadataUi(user: User) {
        binding.profileHeader.apply {
            ivProfileAvatar.load(user.avatarUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }

            tvProfileName.text = user.name
            tvProfileUsername.text = "@${user.login}"
            tvProfileBio.text = user.bio
            tvProfileLocation.text = user.location
            tvProfileReposCount.text = user.publicRepos.toString()
            tvProfileFollowersCount.text = user.followers.toString()
            tvProfileFollowingCount.text = user.following.toString()
        }
    }
}