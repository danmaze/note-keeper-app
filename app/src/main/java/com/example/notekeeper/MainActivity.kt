package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mNoteRecyclerAdapter: NoteRecyclerAdapter? = null
    private var mRecyclerItems: RecyclerView? = null
    private var mNotesLayoutManager: LinearLayoutManager? = null
    private var mCourseRecyclerAdapter: CourseRecyclerAdapter? = null
    private var mCoursesLayoutManager: GridLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { startActivity(Intent(this@MainActivity, NoteActivity::class.java)) }

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false)
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false)
        PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        initializeDisplayContent()
    }

    override fun onResume() {
        super.onResume()
        mNoteRecyclerAdapter!!.notifyDataSetChanged()
        updateNavHeader()
    }

    private fun updateNavHeader() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val textUserName = headerView.findViewById(R.id.text_user_name) as TextView
        val textEmailAddress = headerView.findViewById(R.id.text_email_address) as TextView

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val userName = pref.getString("user_display_name", "")
        val emailAddress = pref.getString("user_email_address", "")

        textUserName.text = userName
        textEmailAddress.text = emailAddress
    }

    private fun initializeDisplayContent() {
        mRecyclerItems = findViewById(R.id.list_items)
        mNotesLayoutManager = LinearLayoutManager(this)
        mCoursesLayoutManager = GridLayoutManager(
            this,
            resources.getInteger(R.integer.course_grid_span)
        )
        val notes = DataManager.notes
        mNoteRecyclerAdapter = NoteRecyclerAdapter(this, notes)
        val courses = DataManager.courses
        mCourseRecyclerAdapter = CourseRecyclerAdapter(this, courses)
        displayNotes()
    }

    private fun displayNotes() {
        mRecyclerItems!!.layoutManager = mNotesLayoutManager
        mRecyclerItems!!.adapter = mNoteRecyclerAdapter
        selectNavigationMenuItem(R.id.nav_notes)
    }

    private fun selectNavigationMenuItem(id: Int) {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val menu = navigationView.menu
        menu.findItem(id).isChecked = true
    }

    private fun displayCourses() {
        mRecyclerItems!!.layoutManager = mCoursesLayoutManager
        mRecyclerItems!!.adapter = mCourseRecyclerAdapter
        selectNavigationMenuItem(R.id.nav_courses)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_notes) {
            displayNotes()
        } else if (id == R.id.nav_courses) {
            displayCourses()
        } else if (id == R.id.nav_share) {
            // handleSelection(R.string.nav_share_message);
            handleShare()
        } else if (id == R.id.nav_send) {
            handleSelection(R.string.nav_send_message)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleShare() {
        val view = findViewById<View>(R.id.list_items)
        Snackbar.make(
            view,
            "Share to - " + PreferenceManager.getDefaultSharedPreferences(this).getString("user_favorite_social", ""),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleSelection(message_id: Int) {
        val view = findViewById<View>(R.id.list_items)
        Snackbar.make(view, message_id, Snackbar.LENGTH_LONG).show()
    }
}