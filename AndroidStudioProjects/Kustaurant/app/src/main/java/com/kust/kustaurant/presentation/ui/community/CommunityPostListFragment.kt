package com.kust.kustaurant.presentation.ui.community

import android.content.Intent
import android.widget.TextView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentCommunityPostListBinding
import com.kust.kustaurant.domain.model.CommunityPost

class CommunityPostListFragment : Fragment() {
    private lateinit var binding: FragmentCommunityPostListBinding
    private val viewModel: CommunityViewModel by activityViewModels()
    private lateinit var commuAdapter: CommunityPostListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()

        // 초기 데이터 로드
        //viewModel.getCommunityPostList(PostLoadState.POST_FIRST_PAGE, "recent")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityPostListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        commuAdapter = CommunityPostListAdapter()
        binding.communityRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = commuAdapter
        }

        commuAdapter.setOnItemClickListener(object : CommunityPostListAdapter.OnItemClickListener{
            override fun onItemClicked(data: CommunityPost) {
                Log.e("CommuItemClicked", data.postId.toString())
                val intent = Intent(requireActivity(), CommunityPostDetailActivity::class.java)
                intent.putExtra("postId", data.postId)
                startActivity(intent)
            }
        })

        setupUI()

        return binding.root
    }

    private fun setupUI() {
        // 커스텀 어댑터 설정
        val spinnerAdapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_community_spinner,
            resources.getStringArray(R.array.community_board_options)
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_community_spinner, parent, false)
                val mainText = view.findViewById<TextView>(R.id.spinner_item_text)
                mainText.text = getItem(position)
                mainText.setTextColor(ContextCompat.getColor(context, R.color.signature_1))
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_community_spinner, parent, false)
                val mainText = view.findViewById<TextView>(R.id.spinner_item_text)
                mainText.text = getItem(position)
                mainText.setTextColor(ContextCompat.getColor(context, R.color.black))
                return view
            }
        }

        binding.communitySpinnerBoard.adapter = spinnerAdapter

        // Spinner 선택 이벤트 처리
        binding.communitySpinnerBoard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedBoard = when (position) {
                    0 -> "all"
                    1 -> "free"
                    2 -> "column"
                    3 -> "suggestion"
                    else -> "all"
                }
                viewModel.onPostCategoryChanged(selectedBoard)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.communityToggleLastestSort.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.updateSortAndLoadPosts("recent")
                binding.communityRecyclerView.scrollToPosition(0)
                binding.communityTogglePopularSort.isChecked = false
                buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))
            } else {
                // 이미 체크된 버튼을 다시 누를 경우 체크를 유지
                if (!binding.communityTogglePopularSort.isChecked) {
                    buttonView.isChecked = true
                } else {
                    buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
                }
            }
        }

        binding.communityTogglePopularSort.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.updateSortAndLoadPosts("popular")
                binding.communityRecyclerView.scrollToPosition(0)
                binding.communityToggleLastestSort.isChecked = false
                buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))
            } else {
                // 이미 체크된 버튼을 다시 누를 경우 체크를 유지
                if (!binding.communityToggleLastestSort.isChecked) {
                    buttonView.isChecked = true
                } else {
                    buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
                }
            }
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

        // SwipeRefreshLayout 새로고침 이벤트 처리
        binding.communitySrl.setOnRefreshListener {

            viewModel.getCommunityPostList(
                PostLoadState.POST_FIRST_PAGE,
                viewModel.sort.value!!
            )

            binding.communitySrl.isRefreshing = false
        }
    }

    private fun setupObservers() {
        viewModel.communityPosts.observe(viewLifecycleOwner) { posts ->
            commuAdapter.submitList(posts)
        }

        viewModel.postCategory.observe(viewLifecycleOwner) { category ->
            val position = when (category) {
                "all" -> 0
                "free" -> 1
                "column" -> 2
                "suggestion" -> 3
                else -> 0
            }
            binding.communitySpinnerBoard.setSelection(position)
        }

        viewModel.sort.observe(viewLifecycleOwner) { sort ->
            binding.communityToggleLastestSort.isChecked = sort == "recent"
            binding.communityTogglePopularSort.isChecked = sort == "popular"
        }
    }
    companion object {
        enum class PostLoadState {
            POST_FIRST_PAGE,
            POST_NEXT_PAGE,
        }
    }
}

