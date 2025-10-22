package com.kust.kustaurant.presentation.ui.community

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.decode.SvgDecoder
import coil.load
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.FragmentCommunityRankingBinding
import com.kust.kustaurant.domain.model.community.CommunityRanking

class CommunityPostRankingFragment : Fragment() {
    private var _binding: FragmentCommunityRankingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CommunityViewModel by activityViewModels()
    private lateinit var adapter: CommunityRankingListAdapter
    private val rankerViews by lazy {
        listOf(
            Triple(binding.communityIvRank1, binding.communityTvRank1Nickname, binding.communityTvRank1CommentCnt),
            Triple(binding.communityIvRank2, binding.communityTvRank2Nickname, binding.communityTvRank2CommentCnt),
            Triple(binding.communityIvRank3, binding.communityTvRank3Nickname, binding.communityTvRank3CommentCnt)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.communityRanking.observe(viewLifecycleOwner) { rankings ->
            val (topRankers, remainingRankings) = viewModel.onRankingDataChanged(rankings)
            bindTopRankers(topRankers)
            adapter.submitList(remainingRankings)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityRankingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.onSortTypeChanged("seasonal")

        setupToggleButtons()

        // RecyclerView 설정
        adapter = CommunityRankingListAdapter()
        binding.communityRvRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CommunityPostRankingFragment.adapter
        }

        return binding.root
    }

    private fun bindTopRankers(topRankers: List<CommunityRanking>) {
        for (i in rankerViews.indices) {
            if (i < 3) {
                if(topRankers.size < i + 1) {
                    rankerViews[i].first.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_baby_cow)
                    )
                    rankerViews[i].second.text = ""
                    rankerViews[i].third.text = "0개"
                    continue
                } else {
                    val (ivRank, tvRankNickname, tvRankCommentCnt) = rankerViews[i]
                    bindRanker(topRankers[i], ivRank, tvRankNickname, tvRankCommentCnt)
                }
            }
        }}

    @SuppressLint("SetTextI18n")
    private fun bindRanker(ranking: CommunityRanking, imageView: ImageView, nicknameTextView: TextView, commentCountTextView: TextView) {
        nicknameTextView.text = ranking.nickname
        commentCountTextView.text = "${ranking.evaluationCount} 개"


        imageView.load(ranking.iconUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_baby_cow)
            error(R.drawable.ic_baby_cow)

            if(ranking.iconUrl.endsWith(".svg", true)) {
                decoderFactory(SvgDecoder.Factory())
            }
        }
    }

    private fun setupToggleButtons() {
        // 초기 상태: 분기순 버튼을 체크하고 누적순 버튼은 체크 해제
        binding.communityToggleQuarterly.isChecked = true
        binding.communityTogglePopularSort.isChecked = false

        binding.communityToggleQuarterly.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))
        binding.communityTogglePopularSort.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))

        // 분기순 버튼 클릭 이벤트 처리
        binding.communityToggleQuarterly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.communityTogglePopularSort.isChecked = false
                viewModel.onSortTypeChanged("seasonal")

                binding.communityToggleQuarterly.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))
                binding.communityTogglePopularSort.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
            } else {
                binding.communityToggleQuarterly.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
            }
        }

        binding.communityTogglePopularSort.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.communityToggleQuarterly.isChecked = false
                viewModel.onSortTypeChanged("cumulative")

                binding.communityTogglePopularSort.setTextColor(ContextCompat.getColor(requireContext(), R.color.signature_1))
                binding.communityToggleQuarterly.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
            } else {
                binding.communityTogglePopularSort.setTextColor(ContextCompat.getColor(requireContext(), R.color.cement_4))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

