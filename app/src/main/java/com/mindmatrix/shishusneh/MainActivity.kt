package com.mindmatrix.shishusneh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import com.mindmatrix.shishusneh.workers.VaccineNotificationWorker
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mindmatrix.shishusneh.data.AppDatabase
import com.mindmatrix.shishusneh.data.AuthRepository
import com.mindmatrix.shishusneh.data.FirestoreSyncRepository
import com.mindmatrix.shishusneh.data.Repository
import com.mindmatrix.shishusneh.data.entity.BabyProfile
import com.mindmatrix.shishusneh.data.entity.FeedingTip
import com.mindmatrix.shishusneh.data.entity.GrowthRecord
import com.mindmatrix.shishusneh.data.entity.Milestone
import com.mindmatrix.shishusneh.data.entity.MilestoneStatus
import com.mindmatrix.shishusneh.data.entity.Vaccination
import com.mindmatrix.shishusneh.domain.HealthData
import com.mindmatrix.shishusneh.domain.ageInWeeks
import com.mindmatrix.shishusneh.domain.ageLabel
import com.mindmatrix.shishusneh.domain.formatDate
import com.mindmatrix.shishusneh.domain.toMillis
import java.time.LocalDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import android.content.Context
import androidx.compose.ui.unit.sp
import com.mindmatrix.shishusneh.data.entity.FeedingRecord
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.Box
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
        val authRepository = AuthRepository()
        val repository = Repository(
            AppDatabase.get(this),
            FirestoreSyncRepository(authRepository)
        )
        setContent {
            ShishuSnehTheme {
                val model: ShishuSnehViewModel = viewModel(
                    factory = ShishuSnehViewModel.factory(repository, authRepository)
                )
                ShishuSnehApp(model)
            }
        }
    }
}

data class UiState(
    val baby: BabyProfile? = null,
    val growth: List<GrowthRecord> = emptyList(),
    val vaccinations: List<Vaccination> = emptyList(),
    val milestones: List<Milestone> = emptyList(),
    val feedingRecords: List<FeedingRecord> = emptyList(),
    val todayTip: FeedingTip? = null,
    val loading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val authError: String? = null,
    val authBusy: Boolean = false
)

class ShishuSnehViewModel(
    private val repository: Repository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
    private var dataJob: Job? = null

    init {
        viewModelScope.launch {
            authRepository.authState().collect { user ->
                _uiState.value = _uiState.value.copy(isLoggedIn = user != null, loading = false)
            }
        }
        viewModelScope.launch {
            repository.ensureSeeded()
            repository.babyProfile.collect { baby ->
                _uiState.value = _uiState.value.copy(baby = baby)
                dataJob?.cancel()
                if (baby != null) observeBabyData(baby)
            }
        }
    }

    fun createProfile(name: String, dob: LocalDate, gender: String, weight: Float, height: Float, hospital: String) {
        viewModelScope.launch {
            repository.saveBaby(
                BabyProfile(
                    name = name.trim(),
                    dateOfBirth = dob.toMillis(),
                    gender = gender,
                    birthWeightKg = weight,
                    birthHeightCm = height,
                    hospitalName = hospital.trim().ifBlank { null }
                )
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authBusy = true, authError = null)
            authRepository.login(email, password)
                .onFailure { _uiState.value = _uiState.value.copy(authError = it.message ?: "Login failed") }
            _uiState.value = _uiState.value.copy(authBusy = false)
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authBusy = true, authError = null)
            authRepository.register(email, password)
                .onFailure { _uiState.value = _uiState.value.copy(authError = it.message ?: "Registration failed") }
            _uiState.value = _uiState.value.copy(authBusy = false)
        }
    }

    fun resetPassword(email: String) {
        authRepository.resetPassword(
            email = email,
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    authError = "Password reset email sent"
                )
            },
            onError = { error ->
                _uiState.value = _uiState.value.copy(
                    authError = error
                )
            }
        )
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = UiState(loading = false)
    }

    fun addGrowth(weight: Float, height: Float, notes: String) {
        val baby = _uiState.value.baby ?: return
        val weeks = ageInWeeks(baby.dateOfBirth)
        viewModelScope.launch {
            repository.addGrowthRecord(
                GrowthRecord(
                    babyId = baby.id,
                    recordDate = System.currentTimeMillis(),
                    weightKg = weight,
                    heightCm = height,
                    ageInWeeks = weeks,
                    weightPercentile = HealthData.estimatedWeightPercentile(weeks, weight),
                    heightPercentile = HealthData.estimatedHeightPercentile(weeks, height),
                    notes = notes.ifBlank { null }
                )
            )
        }
    }
    fun scheduleVaccineNotification(context: Context) {
        val workRequest =
            PeriodicWorkRequestBuilder<VaccineNotificationWorker>(
                1, TimeUnit.DAYS
            ).build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "vaccine_reminder",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun cancelVaccineReminder(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("vaccine_reminder")
    }

    fun getFeedingAdvice(ageMonths: Int): String {
        return when {
            ageMonths <= 1 ->
                """
            🍼 Breastfeeding Guide
            Feed every 2–3 hours
            8–12 feeds per day
            Only breast milk
            No water
            No solid food
            """.trimIndent()

            ageMonths in 2..3 ->
                """
            🍼 Feeding Guide
            Feed every 3 hours
            6–8 feeds/day
            Breast milk or formula
            No solid foods
            No juice
            """.trimIndent()

            ageMonths in 4..5 ->
                """
            🍼 Feeding Guide
            Breastfeeding 5–6 times/day
            Formula optional
            No honey
            No regular solids
            """.trimIndent()

            ageMonths in 6..7 ->
                """
            🍎 Feeding Guide
            Breastfeeding 4–6 times/day
            Banana mash
            Apple puree
            Rice cereal
            Vegetable puree
            Ragi porridge
            """.trimIndent()

            ageMonths in 8..9 ->
                """
            🥣 Feeding Guide
            Breastfeeding 3–5 times/day
            Idli mash
            Khichdi
            Curd
            Soft fruits
            Boiled vegetables
            """.trimIndent()

            else ->
                """
            🍽 Feeding Guide
            Breastfeeding 3–4 times/day
            Rice + dal
            Soft chapati
            Egg yolk
            Paneer
            Fruit pieces
            """.trimIndent()
        }
    }

    fun addFeeding(
        type: String,
        duration: Int,
        amount: String
    ) {
        val baby = _uiState.value.baby ?: return

        viewModelScope.launch {
            repository.addFeedingRecord(
                FeedingRecord(
                    babyId = baby.id,
                    type = type,
                    startTime = System.currentTimeMillis(),
                    durationMinutes = duration,
                    amount = amount
                )
            )
        }
    }

    fun toggleVaccination(vaccination: Vaccination) {
        fun scheduleVaccineNotification(context: Context) {
            val workRequest =
                PeriodicWorkRequestBuilder<VaccineNotificationWorker>(
                    1, TimeUnit.DAYS
                ).build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "vaccine_reminder",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
                )
        }
        viewModelScope.launch {
            repository.updateVaccination(
                vaccination.copy(
                    isCompleted = !vaccination.isCompleted,
                    completedDate = if (!vaccination.isCompleted) System.currentTimeMillis() else null
                )
            )
        }
    }

    fun getMilestoneSuggestion(title: String, completed: Boolean): String {
        if (completed) {
            return when (title) {
                "Smiles socially" -> "🎉 Great progress! Baby is developing well."
                "Sits without support" -> "🎉 Excellent milestone achievement!"
                "Responds to sound" -> "🎉 Healthy sensory development!"
                "Crawls" -> "🎉 Strong motor development!"
                "Walks with support" -> "🎉 Wonderful progress!"
                else -> "🎉 Baby is growing well!"
            }
        }

        return when (title) {
            "Smiles socially" ->
                """
            😊 Suggestions:
            • Talk to baby often
            • Smile and interact face-to-face
            • Use playful expressions
            """.trimIndent()

            "Responds to sound" ->
                """
            🔊 Suggestions:
            • Call baby's name often
            • Use sound toys
            • Play gentle music
            • Consult pediatrician if delayed
            """.trimIndent()

            "Sits without support" ->
                """
            🪑 Suggestions:
            • Daily tummy time
            • Supervised sitting practice
            • Gentle support exercises
            """.trimIndent()

            "Crawls" ->
                """
            🧸 Suggestions:
            • Encourage floor play
            • Place toys slightly away
            • Safe tummy movement practice
            """.trimIndent()

            "Walks with support" ->
                """
            👣 Suggestions:
            • Hold baby's hands while walking
            • Encourage standing
            • Push walker (supervised)
            """.trimIndent()

            else ->
                "Monitor development and consult pediatrician if concerned."
        }
    }

    fun updateMilestone(milestone: Milestone, status: MilestoneStatus) {
        viewModelScope.launch {
            repository.updateMilestone(
                milestone.copy(status = status, checkedDate = System.currentTimeMillis())
            )
        }
    }
    fun saveBabyProfile(
        name: String,
        gender: String,
        birthWeight: String,
        birthHeight: String,
        hospital: String
    ) {
        val currentBaby = _uiState.value.baby ?: return

        viewModelScope.launch {
            repository.saveBabyProfile(
                currentBaby.copy(
                    name = name,
                    gender = gender,
                    birthWeightKg = birthWeight.toFloatOrNull() ?: currentBaby.birthWeightKg,
                    birthHeightCm = birthHeight.toFloatOrNull() ?: currentBaby.birthHeightCm,
                    hospitalName = hospital
                )
            )
        }
    }

    private fun observeBabyData(baby: BabyProfile) {
        dataJob = viewModelScope.launch {
            launch { repository.growthRecords(baby.id).collect { _uiState.value = _uiState.value.copy(growth = it) } }
            launch { repository.vaccinations(baby.id).collect { _uiState.value = _uiState.value.copy(vaccinations = it) } }
            launch { repository.milestones(baby.id).collect { _uiState.value = _uiState.value.copy(milestones = it) } }
            launch {
                repository.feedingRecords(baby.id).collect {
                    _uiState.value = _uiState.value.copy(feedingRecords = it)
                }
            }
            launch {
                val tip = repository.todayTip(baby.id, ageInWeeks(baby.dateOfBirth))
                _uiState.value = _uiState.value.copy(todayTip = tip)
            }
        }
    }

    companion object {
        fun factory(repository: Repository, authRepository: AuthRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                ShishuSnehViewModel(repository, authRepository) as T
        }
    }
}

@Composable
fun ShishuSnehTheme(content: @Composable () -> Unit) {
    val scheme = androidx.compose.material3.lightColorScheme(
        primary = Color(0xFF2E7D6B),
        secondary = Color(0xFFB85C38),
        tertiary = Color(0xFF5D6C89),
        background = Color(0xFFF8FAF8),
        surface = Color.White
    )
    MaterialTheme(colorScheme = scheme, content = content)
}

@Composable
fun ShishuSnehApp(model: ShishuSnehViewModel) {
    val state by model.uiState.collectAsState()
    val nav = rememberNavController()

    LaunchedEffect(state.loading, state.isLoggedIn, state.baby) {
        if (!state.loading) {
            val route = when {
                !state.isLoggedIn -> "auth"
                state.baby == null -> "onboarding"
                else -> "home"
            }
            nav.navigate(route) {
                popUpTo("splash") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = nav, startDestination = "splash") {
        composable("splash") { SplashScreen() }
        composable("auth") { AuthScreen(state, model) }
        composable("onboarding") { OnboardingScreen(model) }
        composable("home") { MainScaffold("home", state, model, nav) }
        composable("growth") { MainScaffold("growth", state, model, nav) }
        composable("vaccines") { MainScaffold("vaccines", state, model, nav) }
        composable("feeding") { MainScaffold("feeding", state, model, nav) }
        composable("milestones") { MainScaffold("milestones", state, model, nav) }
        composable("settings") { MainScaffold("settings", state, model, nav) }
        composable("edit_profile") {
            EditProfileScreen(
                state = state,
                model = model,
                nav = nav
            )
        }
    }
}

@Composable
fun AuthScreen(state: UiState, model: ShishuSnehViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var registerMode by rememberSaveable { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize(), color = Color(0xFFF8FAF8)) {
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Favorite, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(56.dp))
            Spacer(Modifier.height(18.dp))
            Text(if (registerMode) "Create account" else "Login", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Save and sync baby records securely.", color = Color(0xFF60736C))
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            TextButton(
                onClick = {
                    if (email.isNotBlank()) {
                        model.resetPassword(email)
                    }
                }
            ) {
                Text("Forgot Password?")
            }
            if (state.authError != null) {
                Spacer(Modifier.height(10.dp))
                Text(state.authError, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(18.dp))
            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = !state.authBusy,
                onClick = {
                    if (registerMode) model.register(email, password) else model.login(email, password)
                }
            ) {
                Text(if (state.authBusy) "Please wait..." else if (registerMode) "Register" else "Login")
            }
            TextButton(onClick = { registerMode = !registerMode }) {
                Text(if (registerMode) "Already have an account? Login" else "New user? Register")
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(Modifier.fillMaxSize().background(Color(0xFFF1F8F5)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(96.dp).background(Color(0xFF2E7D6B), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Favorite, null, tint = Color.White, modifier = Modifier.size(48.dp))
            }
            Spacer(Modifier.height(18.dp))
            Text("Shishu-Sneh", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Baby health companion", color = Color(0xFF5C6F68))
        }
    }
}

@Composable
fun OnboardingScreen(model: ShishuSnehViewModel) {
    var name by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf(LocalDate.now().minusWeeks(8).toString()) }
    var gender by rememberSaveable { mutableStateOf("Female") }
    var weight by rememberSaveable { mutableStateOf("3.0") }
    var height by rememberSaveable { mutableStateOf("50") }
    var hospital by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    Surface(Modifier.fillMaxSize(), color = Color(0xFFF8FAF8)) {
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Icon(Icons.Default.Favorite, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
            Text("Create baby profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("This keeps growth, vaccinations and milestones available offline.", color = Color(0xFF60736C))
            OutlinedTextField(name, { name = it }, label = { Text("Baby name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(dob, { dob = it }, label = { Text("Date of birth YYYY-MM-DD") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Female", "Male", "Other").forEach {
                    AssistChip(onClick = { gender = it }, label = { Text(it) }, leadingIcon = if (gender == it) {
                        { Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp)) }
                    } else null)
                }
            }
            OutlinedTextField(weight, { weight = it }, label = { Text("Birth weight kg") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(height, { height = it }, label = { Text("Birth height cm") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(hospital, { hospital = it }, label = { Text("Hospital or clinic optional") }, modifier = Modifier.fillMaxWidth())
            if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)
            Button(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = {
                    val parsedDate = runCatching { LocalDate.parse(dob) }.getOrNull()
                    val parsedWeight = weight.toFloatOrNull()
                    val parsedHeight = height.toFloatOrNull()
                    if (name.isBlank() || parsedDate == null || parsedWeight == null || parsedHeight == null) {
                        error = "Please enter valid baby details."
                    } else {
                        model.createProfile(name, parsedDate, gender, parsedWeight, parsedHeight, hospital)
                    }
                }
            ) {
                Text("Start tracking")
            }
        }
    }
}

data class TabItem(val route: String, val label: String, val icon: ImageVector)

val tabs = listOf(
    TabItem("home", "Home", Icons.Default.Home),
    TabItem("growth", "Growth", Icons.Default.ShowChart),
    TabItem("vaccines", "Vaccines", Icons.Default.LocalHospital),
    TabItem("feeding", "Feeding", Icons.Default.Restaurant),
    TabItem("milestones", "Steps", Icons.Default.Star)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(route: String, state: UiState, model: ShishuSnehViewModel, outerNav: NavHostController) {
    val nav = rememberNavController()
    val backStackEntry by nav.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route ?: route
    val baby = state.baby
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shishu-Sneh") },
                actions = {
                    OutlinedButton(onClick = { nav.navigate("settings") }) {
                        Icon(Icons.Default.Tune, null, Modifier.size(18.dp))
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedRoute == tab.route,
                        onClick = { nav.navigate(tab.route) },
                        icon = { Icon(tab.icon, null) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize().background(Color(0xFFF8FAF8))) {
            NavHost(navController = nav, startDestination = route) {
                composable("home") { HomeScreen(state) }
                composable("growth") { if (baby != null) GrowthScreen(state, model) }
                composable("vaccines") { VaccinationScreen(state, model) }
                composable("feeding") { FeedingScreen(state,model) }
                composable("milestones") { MilestoneScreen(state, model) }
                composable("settings") { SettingsScreen(state, model, outerNav) }
            }
        }
    }
}

@Composable
fun HomeScreen(state: UiState) {
    val baby = state.baby
    val latestGrowth = state.growth.lastOrNull()
    val nextVaccine = state.vaccinations.firstOrNull { !it.isCompleted }
    val lastMilestone = state.milestones.lastOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Baby Profile", fontWeight = FontWeight.Bold)

                    Text("Name: ${baby?.name ?: "Not added"}")
                    Text("Gender: ${baby?.gender ?: "-"}")
                    Text("DOB: ${baby?.dateOfBirth?.let { formatDate(it) } ?: "-"}")
                    Text("Weight: ${baby?.birthWeightKg ?: "-"} kg")
                    Text("Height: ${baby?.birthHeightCm ?: "-"} cm")
                    Text("Age: ${baby?.dateOfBirth?.let { ageLabel(it) } ?: "-"}")
                }
            }
        }

        item {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Growth Status", fontWeight = FontWeight.Bold)

                    Text("Latest Weight: ${latestGrowth?.weightKg ?: "-"} kg")
                    Text("Latest Height: ${latestGrowth?.heightCm ?: "-"} cm")
                    Text("Growth Summary: Healthy progress")
                }
            }
        }

        item {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Vaccination Reminder", fontWeight = FontWeight.Bold)

                    Text(
                        "Next Vaccine: ${
                            nextVaccine?.vaccineName ?: "All vaccinations completed"
                        }"
                    )
                }
            }
        }

        item {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Milestone Progress", fontWeight = FontWeight.Bold)

                    Text(
                        "Last Update: ${
                            lastMilestone?.title ?: "No milestone updates"
                        }"
                    )
                }
            }
        }
    }
}

@Composable
fun GrowthScreen(state: UiState, model: ShishuSnehViewModel) {
    var weight by rememberSaveable { mutableStateOf("") }
    var height by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    val latestGrowth = state.growth.lastOrNull()

    val w = weight.toFloatOrNull()
    val h = height.toFloatOrNull()

    val healthStatus =
        if (w != null && h != null) {
            when {
                w < 5f -> "⚠ Underweight"
                w > 12f -> "⚠ Overweight"
                h < 55f -> "⚠ Height below expected range"
                else -> "✅ Healthy growth"
            }
        } else {
            "Enter weight and height"
        }
    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            FeatureCard {
                Text("Growth Analysis", fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(8.dp))

                Text("Status: $healthStatus")

                latestGrowth?.let {
                    Text("Weight: ${it.weightKg} kg")
                    Text("Height: ${it.heightCm} cm")
                    Text("Age: ${it.ageInWeeks} weeks")
                }
            }
        }
        item {
            FeatureCard {
                Text("Add growth record", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(weight, { weight = it }, label = { Text("kg") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                    OutlinedTextField(height, { height = it }, label = { Text("cm") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                }
                OutlinedTextField(notes, { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = {
                    val w = weight.toFloatOrNull()
                    val h = height.toFloatOrNull()
                    if (w != null && h != null) {
                        model.addGrowth(w, h, notes)
                        weight = ""
                        height = ""
                        notes = ""
                    }
                }) { Text("Save entry") }
            }
        }
        item { GrowthChart(state.growth) }
        items(state.growth.reversed()) {
            FeatureCard {
                Text("${it.weightKg} kg - ${it.heightCm} cm", fontWeight = FontWeight.Bold)
                Text("Week ${it.ageInWeeks}, ${formatDate(it.recordDate)}")
                Text("Estimated percentiles: weight ${it.weightPercentile?.toInt()}%, height ${it.heightPercentile?.toInt()}%")
            }
        }
    }
}

@Composable
fun GrowthChart(records: List<GrowthRecord>) {
    FeatureCard {
        Text("Growth trend", fontWeight = FontWeight.Bold)
        Canvas(Modifier.fillMaxWidth().height(170.dp).padding(top = 10.dp)) {
            val axis = Color(0xFFB8C9C2)
            drawLine(axis, Offset(0f, size.height), Offset(size.width, size.height), strokeWidth = 3f)
            drawLine(axis, Offset(0f, 0f), Offset(0f, size.height), strokeWidth = 3f)
            if (records.isNotEmpty()) {
                val maxWeight = (records.maxOf { it.weightKg } + 1f).coerceAtLeast(5f)
                records.forEachIndexed { index, record ->
                    val x = if (records.size == 1) size.width / 2 else index * size.width / (records.size - 1)
                    val y = size.height - (record.weightKg / maxWeight) * size.height
                    drawCircle(Color(0xFF2E7D6B), radius = 9f, center = Offset(x, y))
                    if (index > 0) {
                        val previous = records[index - 1]
                        val px = (index - 1) * size.width / (records.size - 1)
                        val py = size.height - (previous.weightKg / maxWeight) * size.height
                        drawLine(Color(0xFF2E7D6B), Offset(px, py), Offset(x, y), strokeWidth = 5f)
                    }
                }
            } else {
                drawRect(Color(0xFFE6F0EC), size = Size(size.width, size.height))
            }
        }
    }
}

@Composable
fun VaccinationScreen(state: UiState, model: ShishuSnehViewModel) {
    val context = LocalContext.current
    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(state.vaccinations) { vaccine ->

            FeatureCard {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(
                            vaccine.vaccineName,
                            fontWeight = FontWeight.Bold
                        )

                        if (vaccine.isCompleted) {
                            Text("✅ Completed")
                            Text("Done on: ${formatDate(vaccine.completedDate ?: 0)}")
                        } else {
                            val daysLeft =
                                ((vaccine.scheduledDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24))

                            when {
                                daysLeft == 0L -> Text("⚠ Due Today")
                                daysLeft == 1L -> Text("⚠ Tomorrow")
                                daysLeft > 1 -> Text("Due in $daysLeft days")
                                else -> Text("Overdue")
                            }
                        }
                    }

                    Button(
                        onClick = {
                            model.toggleVaccination(vaccine)

                                    if (!vaccine.isCompleted) {

                                    }
                                }
                            ) {
                                Text(
                                    if (vaccine.isCompleted)
                                        "Undo"
                                    else
                                        "Complete"
                                )
                            }
                        }

                    }
                }
            }
        }


@Composable
fun FeedingScreen(state: UiState, model: ShishuSnehViewModel) {
    var type by rememberSaveable { mutableStateOf("Breastfeeding") }
    var duration by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var advice by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            FeatureCard {
                Text("Feeding Tracker", fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Baby age in months") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        val babyAge = age.toIntOrNull() ?: 0
                        advice = model.getFeedingAdvice(babyAge)
                    }
                ) {
                    Text("Get Feeding Advice")
                }
                if (advice.isNotEmpty()) {
                    FeatureCard {
                        Text(
                            advice,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (minutes)") }
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount optional") }
                )

                Button(
                    onClick = {
                        val d = duration.toIntOrNull()
                        if (d != null) {
                            model.addFeeding(type, d, amount)
                            duration = ""
                            amount = ""
                        }
                    }
                ) {
                    Text("Save Feed")
                }
            }
        }

        items(state.feedingRecords) { feed ->
            FeatureCard {
                Text(feed.type, fontWeight = FontWeight.Bold)
                Text("Duration: ${feed.durationMinutes} mins")
                Text("Time: ${formatDate(feed.startTime)}")
                Text("Amount: ${feed.amount ?: "-"}")
            }
        }
    }
}

@Composable
fun MilestoneScreen(state: UiState, model: ShishuSnehViewModel) {
    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(state.milestones) { milestone ->
                FeatureCard {
                    Text(
                        milestone.title,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(milestone.description)

                    Spacer(Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                model.updateMilestone(
                                    milestone,
                                    MilestoneStatus.Achieved
                                )
                            }
                        ) {
                            Text("YES")
                        }

                        OutlinedButton(
                            onClick = {
                                model.updateMilestone(
                                    milestone,
                                    MilestoneStatus.NotYet
                                )
                            }
                        ) {
                            Text("NO")
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        model.getMilestoneSuggestion(
                            milestone.title,
                            milestone.status == MilestoneStatus.Achieved
                        )
                    )
                }
                Text("Week ${milestone.weekNumber}: ${milestone.title}", fontWeight = FontWeight.Bold)
                Text(milestone.description)
            }
        }
    }


@Composable
fun SettingsScreen(
    state: UiState,
    model: ShishuSnehViewModel,
    nav: NavHostController
) {
    val context = LocalContext.current
    val baby = state.baby ?: return

    var vaccineReminder by rememberSaveable { mutableStateOf(true) }
    var selectedLanguage by rememberSaveable { mutableStateOf("English") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {
            FeatureCard {
                Text(
                    "Baby Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(8.dp))

                Text("Name: ${baby.name ?: ""}")
                Text("Gender: ${baby.gender ?: ""}")
                Text("Birth Weight: ${baby.birthWeightKg} kg")
                Text("Birth Height: ${baby.birthHeightCm} cm")
                Text("Hospital: ${baby.hospitalName ?: "Not Added"}")
            }
        }

        item {
            Button(
                onClick = {
                    nav.navigate("edit_profile")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Baby Profile")
            }
        }

        item {
            FeatureCard {
                Text(
                    "Reminder Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Vaccination Reminders")
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = vaccineReminder,
                        onCheckedChange = {
                            vaccineReminder = it

                            if (it) {
                                model.scheduleVaccineNotification(context)
                            } else {
                                model.cancelVaccineReminder(context)
                            }
                        }
                    )
                }

            }

            Button(
                onClick = {
                    model.logout()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }


        }
    }
}


@Composable
fun EditProfileScreen(
    state: UiState,
    model: ShishuSnehViewModel,
    nav: NavHostController
) {
    val baby = state.baby

    var name by rememberSaveable { mutableStateOf(baby?.name ?: "") }

    var gender by rememberSaveable { mutableStateOf(baby?.gender ?: "") }

    var weight by rememberSaveable {
        mutableStateOf(baby?.birthWeightKg?.toString() ?: "")
    }

    var height by rememberSaveable {
        mutableStateOf(baby?.birthHeightCm?.toString() ?: "")
    }

    var hospital by rememberSaveable {
        mutableStateOf(baby?.hospitalName ?: "")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Edit Baby Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Baby Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Birth Weight (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Birth Height (cm)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = hospital,
            onValueChange = { hospital = it },
            label = { Text("Hospital Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                model.saveBabyProfile(
                    name,
                    gender,
                    weight,
                    height,
                    hospital
                )
                nav.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}

@Composable
fun FeatureCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, color = Color(0xFF60736C))
        }
    }
}

@Composable
fun MilestoneButton(label: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) Button(onClick = onClick) { Text(label) } else OutlinedButton(onClick = onClick) { Text(label) }
}
