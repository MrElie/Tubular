package org.schabi.newpipe.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.schabi.newpipe.BuildConfig
import org.schabi.newpipe.R
import org.schabi.newpipe.databinding.ActivityAboutBinding
import org.schabi.newpipe.databinding.FragmentAboutBinding
import org.schabi.newpipe.util.Localization
import org.schabi.newpipe.util.ThemeHelper
import org.schabi.newpipe.util.external_communication.ShareUtils

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Localization.assureCorrectAppLanguage(this)
        super.onCreate(savedInstanceState)
        ThemeHelper.setTheme(this)
        title = getString(R.string.title_activity_about)

        val aboutBinding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(aboutBinding.root)
        setSupportActionBar(aboutBinding.aboutToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        val mAboutStateAdapter = AboutStateAdapter(this)
        // Set up the ViewPager with the sections adapter.
        aboutBinding.aboutViewPager2.adapter = mAboutStateAdapter
        TabLayoutMediator(
            aboutBinding.aboutTabLayout,
            aboutBinding.aboutViewPager2
        ) { tab, position ->
            tab.setText(mAboutStateAdapter.getPageTitle(position))
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class AboutFragment : Fragment() {
        private fun Button.openLink(@StringRes url: Int) {
            setOnClickListener {
                ShareUtils.openUrlInApp(context, requireContext().getString(url))
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            FragmentAboutBinding.inflate(inflater, container, false).apply {
                aboutAppVersion.text = BuildConfig.VERSION_NAME
                return root
            }
        }
    }

    /**
     * A [FragmentStateAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class AboutStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        private val posAbout = 0
        private val posLicense = 1
        private val totalCount = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                posAbout -> AboutFragment()
                posLicense -> LicenseFragment.newInstance(SOFTWARE_COMPONENTS)
                else -> throw IllegalArgumentException("Unknown position for ViewPager2")
            }
        }

        override fun getItemCount(): Int {
            // Show 2 total pages.
            return totalCount
        }

        fun getPageTitle(position: Int): Int {
            return when (position) {
                posAbout -> R.string.tab_about
                posLicense -> R.string.tab_licenses
                else -> throw IllegalArgumentException("Unknown position for ViewPager2")
            }
        }
    }

    companion object {
        /**
         * List of all software components.
         */
        private val SOFTWARE_COMPONENTS = arrayListOf(
            SoftwareComponent(
                "MONZA SAL", "2025", "Elie M.",
                "https://monzasal.com", StandardLicenses.GPL3
            ),
        )
    }
}
