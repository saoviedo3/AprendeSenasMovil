package com.espe.aprendesenasv1

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)

        // Toolbar
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configuración de la navegación
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfig = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.lettersFragment, R.id.conoceMasFragment),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfig)

        // Configurar el Listener del NavigationView
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            // Cerrar el Drawer cuando se seleccione una opción
            drawerLayout.closeDrawer(GravityCompat.START)

            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    // volvemos a inicio
                    navController.navigate(
                        R.id.homeFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(navController.graph.startDestinationId, false)
                            .build()
                    )
                    true
                }
                R.id.lettersFragment -> {
                    // navegamos siempre a Letras, limpiando el backstack
                    navController.navigate(
                        R.id.lettersFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            // vaciamos hasta el root (home), para no duplicar entries
                            .setPopUpTo(navController.graph.startDestinationId, false)
                            .build()
                    )
                    true
                }
                R.id.numbersFragment -> {
                    // navegamos siempre a Letras, limpiando el backstack
                    navController.navigate(
                        R.id.numbersFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            // vaciamos hasta el root (home), para no duplicar entries
                            .setPopUpTo(navController.graph.startDestinationId, false)
                            .build()
                    )
                    true
                }
                R.id.conoceMasFragment -> {
                    // navegamos siempre a Letras, limpiando el backstack
                    navController.navigate(
                        R.id.conoceMasFragment,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            // vaciamos hasta el root (home), para no duplicar entries
                            .setPopUpTo(navController.graph.startDestinationId, false)
                            .build()
                    )
                    true
                }
                else -> false
            }
        }
    }

    // Configuración de navegación hacia atrás
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    fun onConoceMasClick(view: View) {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.conoceMasFragment)
    }
}

