package com.kust.kustaurant.presentation.ui.community


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.R
import com.kust.kustaurant.data.getAccessToken
import com.kust.kustaurant.databinding.FragmentCommunityPostListBinding
import com.kust.kustaurant.databinding.PopupCommuPostListSortBinding
import com.kust.kustaurant.domain.model.CommunityPost
import com.kust.kustaurant.presentation.ui.splash.StartActivity

class CommunityPostListFragment : Fragment() {
    private lateinit var binding: FragmentCommunityPostListBinding
    private val viewModel: CommunityViewModel by activityViewModels()
    private lateinit var commuAdapter: CommunityPostListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityPostListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setupRecyclerView()
        setupUI()

        return binding.root
    }

    private fun setupRecyclerView() {
        commuAdapter = CommunityPostListAdapter().apply {
            setOnItemClickListener(object : CommunityPostListAdapter.OnItemClickListener {
                override fun onItemClicked(data: CommunityPost) {
                    navigateToPostDetail(data.postId)
                }
            })
        }

        binding.communityRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commuAdapter
        }
    }

    private fun navigateToPostDetail(postId: Int) {
        viewModel.selectPost(postId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, CommunityPostDetailFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun setupUI() {
        setupFloatingWriteBtn()

        binding.communityToggleLastestSort.setOnCheckedChangeListener { buttonView, isChecked ->
            handleSortToggle(buttonView, isChecked, "recent", binding.communityTogglePopularSort)
        }
        binding.communityTogglePopularSort.setOnCheckedChangeListener { buttonView, isChecked ->
            handleSortToggle(buttonView, isChecked, "popular", binding.communityToggleLastestSort)
        }

        binding.commuBtnWritePost.setOnClickListener {
            checkToken {
                val intent = Intent(requireActivity(), CommunityPostWriteActivity::class.java)
                startActivity(intent)
            }
        }

        binding.llSelectPostSort.setOnClickListener {
            showPopupWindow()
        }

        binding.communityRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    val postCategory = viewModel.postCategory.value
                    val sort = viewModel.sort.value

                    if (!postCategory.isNullOrEmpty() && !sort.isNullOrEmpty()) {
                        viewModel.getCommunityPostList(PostLoadState.POST_NEXT_PAGE, sort)
                    }
                }
            }
        })

        binding.communitySrl.setOnRefreshListener {
            viewModel.getCommunityPostList(
                PostLoadState.POST_FIRST_PAGE,
                viewModel.sort.value!!
            )

            binding.communitySrl.isRefreshing = false
        }
    }

    private fun handleSortToggle(
        buttonView: CompoundButton,
        isChecked: Boolean,
        sortType: String,
        otherToggle: CompoundButton
    ) {
        if (isChecked) {
            viewModel.updateSortAndLoadPosts(sortType)
            binding.communityRecyclerView.scrollToPosition(0)
            otherToggle.isChecked = false
            buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))
        } else {
            // 유지 조건
            if (!otherToggle.isChecked) {
                buttonView.isChecked = true
            } else {
                buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
            }
        }
    }

    private fun showPopupWindow() {
        val popupBinding = PopupCommuPostListSortBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.showAsDropDown(binding.llSelectPostSort)

        popupBinding.tvAll.setOnClickListener {
            updateSelectedPostSort(popupBinding.tvAll.text.toString(), popupWindow)
        }

        popupBinding.tvFree.setOnClickListener {
            updateSelectedPostSort(popupBinding.tvFree.text.toString(), popupWindow)
        }

        popupBinding.tvSuggest.setOnClickListener {
            updateSelectedPostSort(popupBinding.tvSuggest.text.toString(), popupWindow)
        }

        popupBinding.tvColumn.setOnClickListener {
            updateSelectedPostSort(popupBinding.tvColumn.text.toString(), popupWindow)
        }
    }

    private fun updateSelectedPostSort(selectedText: String, popupWindow: PopupWindow) {
        val categoryCode = when (selectedText) {
            "전체 게시판" -> "all"
            "자유 게시판" -> "free"
            "칼럼 게시판" -> "column"
            "건의 게시판" -> "suggestion"
            else -> "all"
        }

        viewModel.onPostCategoryChanged(categoryCode)
        popupWindow.dismiss()
    }

    private fun setupFloatingWriteBtn() {
        var isHiding = false
        binding.communityRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.commuBtnWritePost.isShown && !isHiding) {
                    isHiding = true
                    binding.commuBtnWritePost.animate()
                        .scaleX(0f)
                        .scaleY(0f)
                        .setDuration(200)
                        .withEndAction {
                            binding.commuBtnWritePost.visibility = View.GONE
                            isHiding = false
                        }
                        .start()
                } else if (dy < 0 && !binding.commuBtnWritePost.isShown) {
                    binding.commuBtnWritePost.visibility = View.VISIBLE
                    binding.commuBtnWritePost.scaleX = 0f
                    binding.commuBtnWritePost.scaleY = 0f
                    binding.commuBtnWritePost.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200)
                        .start()
                }
            }
        })
    }

    fun checkToken(action: () -> Unit) {
        val accessToken = getAccessToken(requireContext())
        if (accessToken == null) {
            val intent = Intent(requireContext(), StartActivity::class.java)
            startActivity(intent)
        } else {
            action()
        }
    }

    private fun setupObservers() {
        viewModel.communityPosts.observe(viewLifecycleOwner) { posts ->
            commuAdapter.submitList(posts)
        }

        viewModel.postCategory.observe(viewLifecycleOwner) { category ->
            val position = when (category) {
                "all" -> "전체 게시판"
                "free" -> "자유 게시판"
                "column" -> "칼럼 게시판"
                "suggestion" -> "건의 게시판"
                else -> "전체 게시판"
            }
            binding.tvSelectPostSort.text = position
        }

        viewModel.sort.observe(viewLifecycleOwner) { sort ->
            binding.communityToggleLastestSort.isChecked = sort == "recent"
            binding.communityTogglePopularSort.isChecked = sort == "popular"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCommunityPostList(
            PostLoadState.POST_FIRST_PAGE,
            viewModel.sort.value!!
        )
    }
    companion object {
        enum class PostLoadState {
            POST_FIRST_PAGE,
            POST_NEXT_PAGE,
        }
    }
}

