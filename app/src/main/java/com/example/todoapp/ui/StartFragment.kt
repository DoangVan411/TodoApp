package com.example.todoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.constant.Constant
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentStartBinding
import com.example.todoapp.ui.adapters.ImageAdapter


class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewPager: ViewPager2
    private lateinit var imageAdapter: ImageAdapter

    private var currentPage = 0

    // Sample data
    private lateinit var titles: List<String>
    private lateinit var contents: List<String>
    private lateinit var images: List<Int>
    private lateinit var loadingImages: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        val view = binding.root

        //gan gia tri cho cac list
        titles = listOf(Constant.TITLE_1, Constant.TITLE_2, Constant.TITLE_3)
        contents = listOf(Constant.CONTENT_1, Constant.CONTENT_2, Constant.CONTENT_3)
        images = listOf(R.drawable.start1, R.drawable.start2, R.drawable.start3)
        loadingImages = listOf(R.drawable.loading1, R.drawable.loading2, R.drawable.loading3)

        //tao imageAdapter cho viewPager2
        imageAdapter = ImageAdapter(images)

        //set up viewpager2
        viewPager = binding.viewPager
        setUpViewPager()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            tvNext.setOnClickListener {
                moveToNextPage()
            }

            tvSkip.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_loginFragment)
            }

        }

    }

    private fun setUpViewPager() {
        viewPager.adapter = imageAdapter
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                currentPage = position
                updateTextViews()
            }
        })
    }

    private fun updateTextViews() {
        if(currentPage in titles.indices) {
            binding.tvTitle.text = titles[currentPage]
            binding.tvContent.text = contents[currentPage]
            binding.ivLoading.setImageResource(loadingImages[currentPage])
        }
        if(currentPage == images.size - 1) {
            binding.tvSkip.text = Constant.BLANK_TEXT
            binding.tvNext.text = Constant.LET_GO
        }
        else {
            binding.tvSkip.text = Constant.SKIP
            binding.tvNext.text = Constant.NEXT
        }
    }

    private fun moveToNextPage() {
        if(currentPage < images.size - 1) {
            currentPage++
            viewPager.setCurrentItem(currentPage, true)
        }
        else if(currentPage == images.size - 1) {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
