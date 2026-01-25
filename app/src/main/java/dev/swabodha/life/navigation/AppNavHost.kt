package dev.swabodha.life.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import dev.swabodha.life.features.FeatureRegistry
import dev.swabodha.life.home.HomeScreen
import dev.swabodha.life.settings.ui.FeatureToggleScreen
import dev.swabodha.life.settings.ui.OssLicensesScreen
import dev.swabodha.life.settings.ui.PrivacyPolicyScreen
import dev.swabodha.life.settings.ui.ReorderHomeTilesScreen
import dev.swabodha.life.settings.ui.SettingsScreen


@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        FeatureRegistry.all().forEach {
            it.registerNavGraph(this)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateToFeatureToggles = {
                    navController.navigate(Routes.FEATURE_TOGGLES)
                },
                onNavigateToReorderHomeTiles = {
                    navController.navigate(Routes.REORDER_HOME_TILES)
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(Routes.PRIVACY_POLICY)
                },
                onNavigateToOssLicenses = {
                    navController.navigate(Routes.OSS_LICENSES)
                }
            )
        }


        composable(Routes.FEATURE_TOGGLES) {
            FeatureToggleScreen()
        }
        composable(Routes.REORDER_HOME_TILES) {
            ReorderHomeTilesScreen(navController)
        }

        composable(Routes.PRIVACY_POLICY) {
            PrivacyPolicyScreen()
        }

        composable(Routes.OSS_LICENSES) {
            OssLicensesScreen()
        }


    }
}
