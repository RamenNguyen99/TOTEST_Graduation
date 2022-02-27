package com.example.totest.ui.listtest

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.totest.R
import com.example.totest.ui.grammardetail.GrammarDetailFragment
import com.example.totest.ui.takingtest.TalkingTestActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_list_test.*

class ListTestActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var progressDialog: ProgressDialog? = null

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_test)
        progressDialog = ProgressDialog(this)
        initDrawerLayout()
        initReferenceFragment(GrammarDetailFragment.getInstance(R.id.itemToeicIntroduction))
        window.statusBarColor = resources.getColor(R.color.colorBlue)
        onClickSetting()
    }

    override fun onBackPressed() = when {
        drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
        else -> initAlertDialog()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.itemPart1 -> {
                initListTestFragment(R.id.itemPart1)
                supportActionBar?.title = getString(R.string.part1)
            }
            R.id.itemPart2 -> {
                initListTestFragment(R.id.itemPart2)
                supportActionBar?.title = getString(R.string.part2)
            }
            R.id.itemPart3 -> {
                initListTestFragment(R.id.itemPart3)
                supportActionBar?.title = getString(R.string.part3)
            }
            R.id.itemPart4 -> {
                initListTestFragment(R.id.itemPart4)
                supportActionBar?.title = getString(R.string.part4)
            }
            R.id.itemPart5Basic -> {
                initListTestFragment(R.id.itemPart5Basic)
                supportActionBar?.title = getString(R.string.part5Basic)
            }
            R.id.itemPart5Intermediate -> {
                initListTestFragment(R.id.itemPart5Intermediate)
                supportActionBar?.title = getString(R.string.part5Intermediate)
            }
            R.id.itemPart5Advanced -> {
                initListTestFragment(R.id.itemPart5Advanced)
                supportActionBar?.title = getString(R.string.part5Advanced)
            }
            R.id.itemPart6 -> {
                initListTestFragment(R.id.itemPart6)
                supportActionBar?.title = getString(R.string.part6)
            }
            R.id.itemPart7 -> {
                initListTestFragment(R.id.itemPart7)
                supportActionBar?.title = getString(R.string.part7)
            }
//            R.id.itemGrammar -> {
//                initReferenceFragment(GrammarListFragment.getInstance(R.id.itemGrammar))
//                supportActionBar?.title = getString(R.string.grammar)
//            }
            R.id.itemToeicIntroduction -> {
                initReferenceFragment(GrammarDetailFragment.getInstance(R.id.itemToeicIntroduction))
                supportActionBar?.title = getString(R.string.toeicIntroduction)
            }
//            R.id.itemWordStudy -> {
//                initReferenceFragment(WordListFragment.getInstance(R.id.itemWordStudy))
//                supportActionBar?.title = getString(R.string.wordStudy)
//            }
        }
        return true
    }

    private fun initListTestFragment(level: Int) = supportFragmentManager.beginTransaction().apply {
        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
        replace(R.id.frListReadingTest, ListTestFragment.getInstance(level))
        commit()
    }

    private fun initReferenceFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
            replace(R.id.frListReadingTest, fragment)
            commit()
        }

    private fun initAlertDialog() = AlertDialog.Builder(this).create().apply {
        setTitle(getString(R.string.confirmExit))
        setMessage(getString(R.string.doYouWantToExit))
        setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no))
        { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes))
        { _, _ ->
            finish()
        }
    }.show()

    fun initProgressDialog() = progressDialog?.apply {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setMessage(getString(R.string.loadingData))
        show()
        setCancelable(false)
    }

    fun dismissProgressDialog() = progressDialog?.dismiss()

    private fun initDrawerLayout() {
        setSupportActionBar(toolBar as Toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolBar as Toolbar,
            R.string.navigationDrawerOpen,
            R.string.navigationDrawerClose
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.title = getString(R.string.toeicIntroduction)
        navigationView.apply {
            setNavigationItemSelectedListener(this@ListTestActivity)
            setCheckedItem(R.id.itemToeicIntroduction)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun notifyNetworkStatus() {
        if (!isNetworkAvailable()) {
            Handler(Looper.getMainLooper()).postDelayed({
                dismissProgressDialog()
                Toast.makeText(this, getString(R.string.networkNotification), Toast.LENGTH_LONG)
                    .show()
            }, 5000)
        }
    }

    private fun onClickSetting() = btnSetting.setOnClickListener {
        startActivity(Intent(this, TalkingTestActivity::class.java))
    }
}