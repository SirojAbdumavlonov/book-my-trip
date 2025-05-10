package com.example.book_my_trip.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.book_my_trip.R
import com.example.book_my_trip.data.model.Flight
import com.example.book_my_trip.ui.components.*
import com.example.book_my_trip.ui.theme.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import java.io.IOException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // States for search inputs with validation
    var fromCity by remember { mutableStateOf("New York") }
    var toCity by remember { mutableStateOf("London") }
    var departureDate by remember { mutableStateOf("15 May 2025") }
    var returnDate by remember { mutableStateOf("22 May 2025") }
    var passengerCount by remember { mutableStateOf("1 Passenger") }

    // Form validation states
    var formHasErrors by remember { mutableStateOf(false) }
    var formErrors by remember { mutableStateOf(mapOf<String, String>()) }

    // Document scanning states
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var extractedPassportData by remember { mutableStateOf<PassportData?>(null) }
    var isProcessingImage by remember { mutableStateOf(false) }

    // State for search results
    var showResults by remember { mutableStateOf(false) }

    // Sample search results
    val searchResults = remember {
        listOf(
            Flight(
                id = "FL-123",
                airlineName = "Emirates Airlines",
                departureTime = "10:40",
                departureAirport = "JFK",
                departureCity = "New York",
                arrivalTime = "14:20",
                arrivalAirport = "LHR",
                arrivalCity = "London",
                price = "$440",
                duration = "7h 40m",
                airlineLogoRes = R.drawable.london
            ),
            Flight(
                id = "FL-456",
                airlineName = "British Airways",
                departureTime = "13:15",
                departureAirport = "JFK",
                departureCity = "New York",
                arrivalTime = "17:30",
                arrivalAirport = "LHR",
                arrivalCity = "London",
                price = "$510",
                duration = "8h 15m",
                airlineLogoRes = R.drawable.london
            ),
            Flight(
                id = "FL-789",
                airlineName = "Virgin Atlantic",
                departureTime = "19:50",
                departureAirport = "JFK",
                departureCity = "New York",
                arrivalTime = "23:55",
                arrivalAirport = "LHR",
                arrivalCity = "London",
                price = "$390",
                duration = "8h 05m",
                airlineLogoRes = R.drawable.london
            )
        )
    }

    // Text recognition for passport scanning - using DisposableEffect for proper lifecycle
    val textRecognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    DisposableEffect(Unit) {
        onDispose {
            textRecognizer.close()
        }
    }

    // Document scanning launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            isProcessingImage = true

            scope.launch {
                try {
                    val inputImage = InputImage.fromFilePath(context, imageUri!!)
                    val passportData = processImageWithTextRecognition(inputImage, textRecognizer)

                    withContext(Dispatchers.Main) {
                        extractedPassportData = passportData
                        isProcessingImage = false

                        // Auto-fill form fields if data was extracted
                        passportData?.let { data ->
                            // Example: Update passenger count based on passport data
                            passengerCount = "1 Passenger (${data.fullName})"
                            snackbarHostState.showSnackbar("Passport data extracted successfully!")
                        } ?: run {
                            snackbarHostState.showSnackbar("Could not extract passport data. Please try again with a clearer image.")
                        }
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        isProcessingImage = false
                        snackbarHostState.showSnackbar("Error processing image: ${e.message}")
                    }
                }
            }
        }
    }

    // Form validation function
    fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()

        if (fromCity.isBlank()) errors["fromCity"] = "Origin city is required"
        if (toCity.isBlank()) errors["toCity"] = "Destination city is required"
        if (departureDate.isBlank()) errors["departureDate"] = "Departure date is required"

        // Additional validation could be added (date format, etc.)

        formErrors = errors
        formHasErrors = errors.isNotEmpty()
        return !formHasErrors
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Search Flights",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            TiniBottomNavigation(navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (!showResults) {
                // Search form
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // From City
                            TiniTextField(
                                value = fromCity,
                                onValueChange = { fromCity = it },
                                label = "From",
                                leadingIcon = Icons.Default.Flight,
                                isError = formErrors.containsKey("fromCity"),
                                errorMessage = formErrors["returnDate"]
                            )

                            // To City
                            TiniTextField(
                                value = toCity,
                                onValueChange = { toCity = it },
                                label = "To",
                                leadingIcon = Icons.Default.FlightLand,
                                isError = formErrors.containsKey("toCity"),
                                errorMessage = formErrors["returnDate"]
                            )

                            // Dates
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Departure Date
                                TiniTextField(
                                    value = departureDate,
                                    onValueChange = { departureDate = it },
                                    label = "Departure",
                                    leadingIcon = Icons.Default.DateRange,
                                    modifier = Modifier.weight(1f),
                                    isError = formErrors.containsKey("departureDate"),
                                    errorMessage = formErrors["departureDate"]
                                )

                                // Return Date
                                TiniTextField(
                                    value = returnDate,
                                    onValueChange = { returnDate = it },
                                    label = "Return",
                                    leadingIcon = Icons.Default.DateRange,
                                    modifier = Modifier.weight(1f),
                                    isError = formErrors.containsKey("returnDate"),
                                    errorMessage = formErrors["returnDate"]
                                )
                            }

                            // Passenger Count
                            TiniTextField(
                                value = passengerCount,
                                onValueChange = { passengerCount = it },
                                label = "Passengers",
                                leadingIcon = Icons.Default.Person,
                                isError = formErrors.containsKey("returnDate"),
                                errorMessage = formErrors["returnDate"]
                            )

                            // Search Button
                            TiniButton(
                                text = "Search Flights",
                                onClick = {
                                    if (validateForm()) {
                                        showResults = true
                                    }
                                }
                            )

                            // Scan Passport Button with loading state
                            OutlinedButton(
                                onClick = {
                                    galleryLauncher.launch("image/*")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Primary
                                ),
                                enabled = !isProcessingImage
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    if (isProcessingImage) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp,
                                            color = Primary
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.DocumentScanner,
                                            contentDescription = "Scan Passport",
                                            tint = Primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isProcessingImage) "Processing..." else "Scan Passport",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Display extracted passport data if available
                            extractedPassportData?.let { data ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Extracted Passport Data",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        PassportDataItem("Name", data.fullName)
                                        PassportDataItem("Passport Number", data.passportNumber)
                                        PassportDataItem("Nationality", data.nationality)
                                        PassportDataItem("Date of Birth", data.dateOfBirth)
                                        PassportDataItem("Expiry Date", data.expiryDate)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Search results
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$fromCity to $toCity",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(
                            onClick = {
                                showResults = false
                            }
                        ) {
                            Text(
                                text = "Edit",
                                style = MaterialTheme.typography.labelMedium,
                                color = Primary
                            )
                        }
                    }

                    Text(
                        text = "$departureDate · $passengerCount",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                items(searchResults) { flight ->
                    FlightCard(
                        airlineName = flight.airlineName,
                        departureTime = flight.departureTime,
                        departureAirport = flight.departureAirport,
                        arrivalTime = flight.arrivalTime,
                        arrivalAirport = flight.arrivalAirport,
                        price = flight.price,
                        duration = flight.duration,
                        airlineLogoRes = flight.airlineLogoRes,
                        onClick = {
                            // Navigate to flight details screen
                            scope.launch {
                                snackbarHostState.showSnackbar("Selected flight: ${flight.id}")
                                // In a real app: navController.navigate("flight_details/${flight.id}")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PassportDataItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// Improved document scanning function with coroutines
private suspend fun processImageWithTextRecognition(
    inputImage: InputImage,
    textRecognizer: com.google.mlkit.vision.text.TextRecognizer
): PassportData? {
    return try {
        val visionText = textRecognizer.process(inputImage).await()
        extractPassportData(visionText)
    } catch (e: Exception) {
        null
    }
}

private fun extractPassportData(visionText: Text): PassportData? {
    // Full text from recognition
    val fullText = visionText.text.uppercase() // Convert to uppercase for more reliable matching

    // Initialize variables to store passport data
    var fullName = ""
    var passportNumber = ""
    var nationality = ""
    var dateOfBirth = ""
    var expiryDate = ""

    // Improved regex patterns for passport data extraction

    // For MRZ (Machine Readable Zone) - typically at bottom of passport
    // P<USASMITH<<JOHN<MICHAEL<<<<<<<<<<<<<<<<<<<<<<
    // 1234567890USA8001015M2501012<<<<<<<<<<<<<<00
    val mrzPattern = "P[<A-Z]{3}([A-Z<]+)<<".toRegex()
    mrzPattern.find(fullText)?.let { result ->
        val potentialName = result.groupValues[1].replace("<", " ").trim()
        if (potentialName.isNotEmpty()) {
            fullName = potentialName
        }
    }

    // More robust passport number pattern
    // Look for common prefixes and formats
    val passportNoPatterns = listOf(
        "(?:PASSPORT NO|PASSPORT NUMBER|DOCUMENT NO|NO|NUMBER)[.:]?\\s*([A-Z0-9]{6,12})".toRegex(RegexOption.IGNORE_CASE),
        "(?:PASSEPORT|REISEPASS|PASAPORTE)[.:]?\\s*([A-Z0-9]{6,12})".toRegex(RegexOption.IGNORE_CASE),
        "(?:P|PASS)#\\s*([A-Z0-9]{6,12})".toRegex(RegexOption.IGNORE_CASE)
    )

    // Using a regular for loop with index to avoid using break in lambda
    var foundPassportNumber = false
    for (i in passportNoPatterns.indices) {
        if (foundPassportNumber) continue

        passportNoPatterns[i].find(fullText)?.let {
            passportNumber = it.groupValues[1].trim()
            foundPassportNumber = true
        }
    }

    // More robust name pattern
    val namePatterns = listOf(
        "(?:NAME|FULL NAME|SURNAME AND GIVEN NAMES)[.:]?\\s*([A-Za-z\\s,]+)".toRegex(RegexOption.IGNORE_CASE),
        "(?:SURNAME|GIVEN NAMES)[.:]?\\s*([A-Za-z\\s,]+)".toRegex(RegexOption.IGNORE_CASE)
    )

    // Using a regular for loop with index to avoid using break in lambda
    var foundName = false
    for (i in namePatterns.indices) {
        if (foundName) continue

        namePatterns[i].find(fullText)?.let {
            val extractedName = it.groupValues[1].trim()
            if (extractedName.length > fullName.length) { // Take the longer name if multiple matches
                fullName = extractedName
                foundName = true
            }
        }
    }

    // Nationality patterns
    val nationalityPatterns = listOf(
        "(?:NATIONALITY|NATIONALITÉ|NATIONALITE)[.:]?\\s*([A-Za-z\\s]+)".toRegex(RegexOption.IGNORE_CASE),
        "(?:CITIZEN OF|CITIZENSHIP)[.:]?\\s*([A-Za-z\\s]+)".toRegex(RegexOption.IGNORE_CASE)
    )

    // Using a regular for loop with index to avoid using break in lambda
    var foundNationality = false
    for (i in nationalityPatterns.indices) {
        if (foundNationality) continue

        nationalityPatterns[i].find(fullText)?.let {
            nationality = it.groupValues[1].trim()
            foundNationality = true
        }
    }

    // Date patterns - look for common date formats
    // Consider multiple date formats (DD/MM/YYYY, MM/DD/YYYY, YYYY/MM/DD)
    val datePatterns = listOf(
        "(?:DATE OF BIRTH|BIRTH DATE|DOB|BIRTH)[.:]?\\s*(\\d{1,2}[/.-]\\d{1,2}[/.-]\\d{2,4})".toRegex(RegexOption.IGNORE_CASE),
        "(?:DATE OF BIRTH|BIRTH DATE|DOB|BIRTH)[.:]?\\s*(\\d{2,4}[/.-]\\d{1,2}[/.-]\\d{1,2})".toRegex(RegexOption.IGNORE_CASE),
        "(?:BORN|BIRTH)[.:]?\\s*(\\d{1,2}\\s+[A-Za-z]{3,}\\s+\\d{2,4})".toRegex(RegexOption.IGNORE_CASE) // Format: 01 Jan 1990
    )

    // Using a regular for loop with index to avoid using break in lambda
    var foundDOB = false
    for (i in datePatterns.indices) {
        if (foundDOB) continue

        datePatterns[i].find(fullText)?.let {
            dateOfBirth = it.groupValues[1].trim()
            foundDOB = true
        }
    }

    // Expiry date patterns
    val expiryPatterns = listOf(
        "(?:DATE OF EXPIRY|EXPIRY DATE|EXPIRATION|EXPIRY|EXPIRES|VALID UNTIL)[.:]?\\s*(\\d{1,2}[/.-]\\d{1,2}[/.-]\\d{2,4})".toRegex(RegexOption.IGNORE_CASE),
        "(?:DATE OF EXPIRY|EXPIRY DATE|EXPIRATION|EXPIRY|EXPIRES|VALID UNTIL)[.:]?\\s*(\\d{2,4}[/.-]\\d{1,2}[/.-]\\d{1,2})".toRegex(RegexOption.IGNORE_CASE),
        "(?:EXPIRY|EXPIRES|VALID UNTIL)[.:]?\\s*(\\d{1,2}\\s+[A-Za-z]{3,}\\s+\\d{2,4})".toRegex(RegexOption.IGNORE_CASE) // Format: 01 Jan 2030
    )

    // Using a regular for loop with index to avoid using break in lambda
    var foundExpiry = false
    for (i in expiryPatterns.indices) {
        if (foundExpiry) continue

        expiryPatterns[i].find(fullText)?.let {
            expiryDate = it.groupValues[1].trim()
            foundExpiry = true
        }
    }

    // Process each text block separately for better contextual extraction
    for (textBlock in visionText.textBlocks) {
        val blockText = textBlock.text.uppercase()

        // Check for common passport field formats
        if (blockText.contains("NAME") && fullName.isEmpty()) {
            val lines = blockText.split("\n")
            for (i in 0 until lines.size - 1) {
                if (lines[i].contains("NAME")) {
                    fullName = lines[i+1].trim()
                    break
                }
            }
        }

        // Similar checks for other fields
        if (blockText.contains("PASSPORT") && passportNumber.isEmpty()) {
            val lines = blockText.split("\n")
            for (i in 0 until lines.size - 1) {
                if (lines[i].contains("PASSPORT")) {
                    val potentialNumber = lines[i+1].trim()
                    if (potentialNumber.matches("[A-Z0-9]{6,12}".toRegex())) {
                        passportNumber = potentialNumber
                        break
                    }
                }
            }
        }
    }

    // Return the extracted data if we found anything useful
    return if (fullName.isNotEmpty() || passportNumber.isNotEmpty()) {
        PassportData(
            fullName = fullName.ifEmpty { "Not found" },
            passportNumber = passportNumber.ifEmpty { "Not found" },
            nationality = nationality.ifEmpty { "Not found" },
            dateOfBirth = dateOfBirth.ifEmpty { "Not found" },
            expiryDate = expiryDate.ifEmpty { "Not found" }
        )
    } else {
        null // Return null if we couldn't extract meaningful data
    }
}

// Data class to hold extracted passport information
data class PassportData(
    val fullName: String,
    val passportNumber: String,
    val nationality: String,
    val dateOfBirth: String,
    val expiryDate: String
)