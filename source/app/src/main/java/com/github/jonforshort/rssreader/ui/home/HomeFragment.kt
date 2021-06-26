//
// MIT License
//
// Copyright (c) 2021
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package com.github.jonforshort.rssreader.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.github.jonforshort.rssreader.R
import com.github.jonforshort.rssreader.utils.setActionBarTitle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

internal class HomeFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private lateinit var homeTabs: ViewPager2
    private lateinit var homeTabsAdapter: HomeTabsAdapter
    private lateinit var homeTabsLayout: TabLayout
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeTabs = view.findViewById(R.id.homeTabs)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeTabsAdapter = HomeTabsAdapter(this, homeViewModel)
        homeTabs.adapter = homeTabsAdapter
        homeTabsLayout = view.findViewById(R.id.homeTabsLayout)
        homeTabsLayout.addOnTabSelectedListener(this)

        attachTabsToTabLayout()
    }

    private fun attachTabsToTabLayout() {
        TabLayoutMediator(homeTabsLayout, homeTabs) { tab, position ->
            tab.icon = AppCompatResources.getDrawable(
                requireContext(), homeViewModel.tabs[position].icon
            )
        }.attach()
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        val selectedTabPosition = tab.position
        val actionBarTitle = homeViewModel.tabs[selectedTabPosition].text
        setActionBarTitle(actionBarTitle)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
    }
}

private class HomeTabsAdapter(
    fragment: Fragment,
    val homeViewModel: HomeViewModel
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = homeViewModel.tabs.size

    override fun createFragment(position: Int): Fragment {
        val fragment = FeedFragment()
        fragment.arguments = FeedFragment.encodeFragmentArguments(
            homeViewModel.tabs[position].feedType
        )
        return fragment
    }
}
