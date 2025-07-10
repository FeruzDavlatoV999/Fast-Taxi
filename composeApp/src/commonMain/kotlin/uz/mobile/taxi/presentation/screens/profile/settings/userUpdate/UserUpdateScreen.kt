package uz.mobile.taxi.presentation.screens.profile.settings.userUpdate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import chaintech.network.connectivitymonitor.ConnectivityStatus
import uz.mobile.taxi.domain.util.camera.PermissionCallback
import uz.mobile.taxi.domain.util.camera.PermissionStatus
import uz.mobile.taxi.domain.util.camera.PermissionType
import uz.mobile.taxi.domain.util.camera.createPermissionsManager
import uz.mobile.taxi.domain.util.camera.detectMimeType
import uz.mobile.taxi.domain.util.camera.imageBitmapToByteArray
import uz.mobile.taxi.domain.util.camera.rememberGalleryManager
import uz.mobile.taxi.domain.util.camera.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import taxi.composeapp.generated.resources.*
import network.chaintech.cmpimagepickncrop.utils.AlertMessageDialog
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import rememberCameraManager
import uz.mobile.taxi.presentation.AppScreenModel
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.components.CheckEditableField
import uz.mobile.taxi.presentation.components.CustomLogOutDialog
import uz.mobile.taxi.presentation.components.HeaderTitle
import uz.mobile.taxi.presentation.components.InternetOffline
import uz.mobile.taxi.presentation.components.MainTopBar
import uz.mobile.taxi.presentation.components.ProfileEditButton
import uz.mobile.taxi.presentation.components.ProfileUserAvatar
import uz.mobile.taxi.presentation.components.ProfileUserFieldsEditText
import uz.mobile.taxi.presentation.components.WheelDatePickerBottomSheet
import uz.mobile.taxi.presentation.platform.MoviesAppScreen
import uz.mobile.taxi.presentation.theme.deleteTextColor
import uz.mobile.taxi.presentation.theme.getTypography
import uz.mobile.taxi.presentation.util.getNavigationBarHeight


class UserUpdateScreen : MoviesAppScreen {
    @Composable
    override fun Content() {
        val networkScreenModel = remember { NetworkScreenModel() }
        val connectivityStatus by networkScreenModel.connectivityStatus.collectAsState()
        when (connectivityStatus) {
            ConnectivityStatus.NOT_CONNECTED -> InternetOffline(tryAgain = { networkScreenModel.refresh() })
            else -> {
                UserUpdate()
            }
        }
    }
}

@Composable
fun UserUpdate(
    viewModel: UserUpdateViewModel = koinInject(),
    appViewModel: AppScreenModel = koinInject(),
    navigator: Navigator = LocalNavigator.currentOrThrow,
) {

    LaunchedEffect(Unit) { viewModel.getUser() }

    var isEditing by remember { mutableStateOf(false) }
    val keyboardManager = LocalSoftwareKeyboardController.current

    val coroutineScope = rememberCoroutineScope()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageBitmapByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var imageSourceOptionDialog by remember { mutableStateOf(value = false) }
    var launchCamera by remember { mutableStateOf(value = false) }
    var launchGallery by remember { mutableStateOf(value = false) }
    var launchSetting by remember { mutableStateOf(value = false) }
    var permissionRationalDialog by remember { mutableStateOf(value = false) }
    var showGenderSheet by remember { mutableStateOf(value = false) }
    var showDatePicker by remember { mutableStateOf(value = false) }
    var showLogOutDialog by remember { mutableStateOf(false) }

    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus,
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> launchCamera = true
                        PermissionType.GALLERY -> launchGallery = true
                    }
                }

                else -> {
                    permissionRationalDialog = true
                }
            }
        }
    })

    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
            imageBitmapByteArray = imageBitmap?.toByteArray()
        }
    }

    val galleryManager = rememberGalleryManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
            imageBitmapByteArray = imageBitmap?.toByteArray()
        }
    }

    if (imageSourceOptionDialog) {
        ImageSourceOptionBottomSheet(onDismissRequest = {
            imageSourceOptionDialog = false
        }, onGalleryRequest = {
            imageSourceOptionDialog = false
            launchGallery = true
        }, onCameraRequest = {
            imageSourceOptionDialog = false
            launchCamera = true
        })
    }

    if (launchGallery) {
        if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
            galleryManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.GALLERY)
        }
        launchGallery = false
    }

    if (launchCamera) {
        if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
            cameraManager.launch()
        } else {
            permissionsManager.askPermission(PermissionType.CAMERA)
        }
        launchCamera = false
    }

    if (launchSetting) {
        permissionsManager.launchSettings()
        launchSetting = false
    }

    if (permissionRationalDialog) {
        AlertMessageDialog(title = "Permission Required",
            message = "To set your profile picture, please grant this permission. You can manage permissions in your device settings.",
            positiveButtonText = "Settings",
            negativeButtonText = "Cancel",
            onPositiveClick = {
                permissionRationalDialog = false
                launchSetting = true
            },
            onNegativeClick = {
                permissionRationalDialog = false
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .pointerInput(Unit) {
                detectTapGestures {
                    keyboardManager?.hide()
                }
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        MainTopBar(onBackButtonClicked = {
            navigator.pop()
        }, text = "Profile")

        LazyColumn(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 16.dp)) {

            item {
                Spacer(modifier = Modifier.height(23.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    ProfileUserAvatar(
                        modifier = Modifier.align(Alignment.Center).size(92.dp),
                        imageUri = viewModel.userImage,
                        imageBitmap = imageBitmap,
                        userName = viewModel.userName,
                        isEdit = isEditing,
                        onEditClick = {
                            imageSourceOptionDialog = true
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
                HeaderTitle(stringResource(Res.string.name))
                Spacer(modifier = Modifier.height(8.dp))
                ProfileUserFieldsEditText(
                    value = viewModel.userName,
                    enabled = isEditing,
                    onValueChange = { viewModel.updateUserName(it) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HeaderTitle(stringResource(Res.string.number_phone))
                Spacer(modifier = Modifier.height(8.dp))
                ProfileUserFieldsEditText(
                    value = "+${viewModel.phoneNumber}",
                    enabled = false,
                    onValueChange = { viewModel.updatePhoneNumber(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HeaderTitle(stringResource(Res.string.gender))
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                CheckEditableField(
                    gender = if (viewModel.gender == "male") stringResource(Res.string.man) else stringResource(
                        Res.string.woman
                    ),
                    isEditable = isEditing,
                    isIcon = true,
                    onGenderClick = {
                        if (isEditing) {
                            showGenderSheet = true
                        }
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HeaderTitle(stringResource(Res.string.date_of_birth))
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                CheckEditableField(
                    gender = viewModel.dateOfBirth,
                    isEditable = isEditing,
                    isIcon = false,
                    onGenderClick = {
                        if (isEditing) {
                            showDatePicker = true
                        }
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))

                if (isEditing) {
                    Spacer(modifier = Modifier.height(51.dp))
                }

                ProfileEditButton(
                    text = if (isEditing) stringResource(Res.string.save_changes) else stringResource(
                        Res.string.edit
                    ),
                    onClick = {
                        val mimeType = imageBitmap?.let { detectMimeType(it) }
                        val bitmap = imageBitmap?.let { imageBitmapToByteArray(it) }


                        if (isEditing) {
                            viewModel.updateUser(
                                viewModel.userName,
                                viewModel.gender,
                                viewModel.dateOfBirth,
                                imageBitmapByteArray,
                                mimeType
                            )
                        }
                        isEditing = !isEditing
                    }
                )
            }

            item {
                if (!isEditing) {
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileEditButton(
                        text = stringResource(Res.string.delete_account),
                        onClick = { showLogOutDialog = true },
                        textStyle = getTypography().labelMedium.copy(color = deleteTextColor)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height((75 + getNavigationBarHeight()).dp))
            }
        }
    }

    if (showGenderSheet) {
        GenderSelectionBottomSheet(
            currentGender = viewModel.gender,
            onGenderSelected = { selectedGender ->
                viewModel.updateGender(selectedGender)
                showGenderSheet = false
            },
            onDismiss = { showGenderSheet = false }
        )
    }
    if (showDatePicker) {
        WheelDatePickerBottomSheet(
            showDatePicker = true,
            onSelect = { date ->
                viewModel.updateDateOfBirth(date)
                showDatePicker = false
            },
            onClicked = { showDatePicker = false })
    }
    if (showLogOutDialog) {
        CustomLogOutDialog(
            onConfirm = {
                showLogOutDialog = false
                appViewModel.logout()
            },
            onDismiss = { showLogOutDialog = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectionBottomSheet(
    currentGender: String,
    onGenderSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(Unit) {
        coroutineScope.launch { bottomSheetState.show() }
    }

    var selectedGender by remember { mutableStateOf(currentGender) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(Res.string.close),
                        style = TextStyle(
                            fontSize = 15.38.sp,
                            lineHeight = 23.07.sp,
                            fontFamily = FontFamily(Font(Res.font.inter_light)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF393E54)
                        )
                    )
                }

                Text(
                    text = stringResource(Res.string.gender),
                    color = Color.Black,
                    fontSize = 16.sp,
                    style = TextStyle(
                        fontSize = 15.38.sp,
                        lineHeight = 23.07.sp,
                        fontFamily = FontFamily(Font(Res.font.inter_bold)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF070E29)
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                TextButton(onClick = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    onGenderSelected(selectedGender)  // Pass the selected gender when "Выбрать" is clicked
                }) {
                    Text(
                        text = stringResource(Res.string.choose),
                        style = TextStyle(
                            fontSize = 15.38.sp,
                            lineHeight = 23.07.sp,
                            fontFamily = FontFamily(Font(Res.font.inter_light)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF393E54)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                GenderRadioButton(
                    selected = selectedGender == "male",  // Check if "male" is selected
                    label = stringResource(Res.string.man),  // Male
                    onSelect = { selectedGender = "male" }  // Set selected gender to "male"
                )
                Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.5.dp)
                GenderRadioButton(
                    selected = selectedGender == "female",  // Check if "female" is selected
                    label = stringResource(Res.string.woman),  // Female
                    onSelect = { selectedGender = "female" }  // Set selected gender to "female"
                )
                Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun GenderRadioButton(
    selected: Boolean,
    label: String,
    onSelect: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onSelect() }  // Update selected gender when clicked
    ) {
        RadioButton(
            selected = selected,
            onClick = { onSelect() },
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6BF1F6))  // Use the theme's primary color for selected
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourceOptionBottomSheet(
    onDismissRequest: () -> Unit,
    onGalleryRequest: () -> Unit = {},
    onCameraRequest: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedOption by remember { mutableStateOf("gallery") }

    LaunchedEffect(Unit) {
        coroutineScope.launch { sheetState.show() }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 30.75561.dp, topEnd = 30.75561.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(Res.string.close), color = Color.Black,
                        style = TextStyle(
                            fontSize = 15.38.sp,
                            lineHeight = 23.07.sp,
                            fontFamily = FontFamily(Font(Res.font.inter_light)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF393E54),
                        )
                    )
                }
                Text(
                    text = stringResource(Res.string.profile_photo),
                    color = Color(0xFF070E29),
                    style = TextStyle(
                        fontSize = 15.38.sp,
                        lineHeight = 23.07.sp,
                        fontFamily = FontFamily(Font(Res.font.inter_bold)),
                        fontWeight = FontWeight(700),
                    )
                )
                TextButton(onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                        if (selectedOption == "gallery") {
                            onGalleryRequest()
                        } else {
                            onCameraRequest()
                        }
                    }
                }) {
                    Text(
                        text = stringResource(Res.string.choose), color = Color.Black,
                        style = TextStyle(
                            fontSize = 15.38.sp,
                            lineHeight = 23.07.sp,
                            fontFamily = FontFamily(Font(Res.font.inter_light)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF393E54),
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.5.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { selectedOption = "gallery" })
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == "gallery",
                    onClick = { selectedOption = "gallery" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF6BF1F6),
                        unselectedColor = MaterialTheme.colors.onSurface
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.download_from_gallery),
                    color = Color(0xFF393E54)
                )
            }

            Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.5.dp)

            // Option 2: Camera
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { selectedOption = "camera" })
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == "camera",
                    onClick = { selectedOption = "camera" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF6BF1F6),
                        unselectedColor = MaterialTheme.colors.onSurface
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(Res.string.take_photo), color = Color(0xFF393E54))
            }

            Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.5.dp)
        }
    }
}




