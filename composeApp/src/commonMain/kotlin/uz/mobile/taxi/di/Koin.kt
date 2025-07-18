package uz.mobile.taxi.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
//import sp.bvantur.inspektify.ktor.InspektifyKtor
//import sp.bvantur.inspektify.ktor.PresentationType
import uz.mobile.taxi.cachingModule
import uz.mobile.taxi.data.remote.service.AuthService
import uz.mobile.taxi.data.remote.service.MoviesService
import uz.mobile.taxi.data.remote.service.NotificationService
import uz.mobile.taxi.data.remote.service.UserService
import uz.mobile.taxi.data.remote.service.impl.AuthServiceImpl
import uz.mobile.taxi.data.remote.service.impl.MoviesServiceImpl
import uz.mobile.taxi.data.remote.service.impl.NotificationServiceImpl
import uz.mobile.taxi.data.remote.service.impl.UserServiceImpl
import uz.mobile.taxi.data.repository.AuthRepository
import uz.mobile.taxi.data.repository.MovieRepository
import uz.mobile.taxi.data.repository.NotificationRepository
import uz.mobile.taxi.data.repository.SearchRepository
import uz.mobile.taxi.data.repository.UserRepository
import uz.mobile.taxi.data.repository.impl.AuthRepositoryImpl
import uz.mobile.taxi.data.repository.impl.MovieRepositoryImpl
import uz.mobile.taxi.data.repository.impl.NotificationRepositoryImpl
import uz.mobile.taxi.data.repository.impl.SearchRepositoryImpl
import uz.mobile.taxi.data.repository.impl.UserRepositoryImpl
import uz.mobile.taxi.datastore.CachingManager
import uz.mobile.taxi.datastore.LanguageDataStore
import uz.mobile.taxi.domain.model.Movie
import uz.mobile.taxi.domain.util.ResponseHandler
import uz.mobile.taxi.domain.validation.ValidateName
import uz.mobile.taxi.domain.validation.ValidatePassword
import uz.mobile.taxi.domain.validation.ValidatePhone
import uz.mobile.taxi.domain.validation.ValidationPayment
import uz.mobile.taxi.platformModule
import uz.mobile.taxi.presentation.AppScreenModel
import uz.mobile.taxi.presentation.NetworkScreenModel
import uz.mobile.taxi.presentation.screens.auth.forgotPasswordChange.ForgotPasswordChangePasswordViewModel
import uz.mobile.taxi.presentation.screens.auth.forgotPasswordOtp.ForgetPasswordOtpViewModel
import uz.mobile.taxi.presentation.screens.auth.forgotPasswordPhone.ForgotPasswordPhoneViewModel
import uz.mobile.taxi.presentation.screens.auth.login.LoginScreenViewModel
import uz.mobile.taxi.presentation.screens.auth.registerName.RegisterNameViewModel
import uz.mobile.taxi.presentation.screens.auth.registerOtp.RegisterOtpScreenViewModel
import uz.mobile.taxi.presentation.screens.auth.registerPassword.RegisterPasswordViewModel
import uz.mobile.taxi.presentation.screens.auth.registerPhone.RegisterPhoneViewModel
import uz.mobile.taxi.presentation.screens.category.CategoryViewModel
import uz.mobile.taxi.presentation.screens.detailsScreen.DetailsScreenViewModel
import uz.mobile.taxi.presentation.screens.home.HomeScreenViewModel
import uz.mobile.taxi.presentation.screens.live.LiveScreenViewModel
import uz.mobile.taxi.presentation.screens.comments.CommentsScreenModel
import uz.mobile.taxi.presentation.screens.notifications.NotificationsScreenModel
import uz.mobile.taxi.presentation.screens.onboarding.OnboardingScreenViewModel
import uz.mobile.taxi.presentation.screens.play.PlayScreenViewModel
import uz.mobile.taxi.presentation.screens.pricing.PricingPlanScreenModel
import uz.mobile.taxi.presentation.screens.profile.ProfileViewModel
import uz.mobile.taxi.presentation.screens.profile.billing.PaymentScreenViewModel
import uz.mobile.taxi.presentation.screens.profile.billing.balanceReplanishment.BalanceReplenishmentViewModel
import uz.mobile.taxi.presentation.screens.profile.billing.biilingScreen.BillingScreenViewModel
import uz.mobile.taxi.presentation.screens.profile.billing.paymentTariff.PaymentTariffViewModel
import uz.mobile.taxi.presentation.screens.profile.billing.planBilling.PlanScreenViewModel
import uz.mobile.taxi.presentation.screens.profile.settings.changePassword.forgotPasswordChange.ProfileForgotPasswordChangePasswordViewModel
import uz.mobile.taxi.presentation.screens.profile.settings.changePassword.forgotPasswordOtp.ProfileForgetPasswordOtpViewModel
import uz.mobile.taxi.presentation.screens.profile.settings.changePassword.forgotPasswordPhone.ProfileForgotPasswordPhoneViewModel
import uz.mobile.taxi.presentation.screens.profile.settings.language.LanguageViewModel
import uz.mobile.taxi.presentation.screens.profile.settings.privacy.PrivacyViewModel
import uz.mobile.taxi.presentation.screens.profile.settings.userUpdate.UserUpdateViewModel
import uz.mobile.taxi.presentation.screens.search.SearchScreenViewModel
import uz.mobile.taxi.presentation.sharedviewmodel.FullScreenStateModel
import kotlin.jvm.JvmName

@JvmName("doInitKoin")
fun initKoin(
    enableNetworkLogs: Boolean = true,
    baseUrl: String,
    appDeclaration: KoinAppDeclaration = {},
) {
    startKoin {
        appDeclaration()
        modules(
            commonModule(enableNetworkLogs = enableNetworkLogs, baseUrl),
        )
    }
}

fun initKoin(baseUrl: String) = initKoin(enableNetworkLogs = false, baseUrl = baseUrl) {

}


fun commonModule(
    enableNetworkLogs: Boolean,
    baseUrl: String,
) = platformModule() + cachingModule() +
        getDataModule(baseUrl) +
        getUseCaseModule() +
        getScreenModelModule() +
        createHttpClient(enableNetworkLogs)

fun getScreenModelModule() = module {
    single { AppScreenModel(get(), get(), get()) }
    single { NetworkScreenModel() }

    single { HomeScreenViewModel(get()) }
    factory { PaymentScreenViewModel() }
    factory { PlanScreenViewModel(get()) }
    factory { BalanceReplenishmentViewModel(get(), get(), get()) }
    factory { (movieId: Int) -> BillingScreenViewModel(get(), get(), id = movieId) }
    factory { PaymentTariffViewModel(get()) }
    single { CategoryViewModel(get()) }
    factory { (movieId: Int) -> DetailsScreenViewModel(movieId, get()) }
    factory { (movie: Movie) -> PlayScreenViewModel(movie, get()) }


    single { SearchScreenViewModel(get()) }
    factory { (movieId: Int) -> CommentsScreenModel(movieId, get()) }


    factory { ProfileViewModel(get(), get(), get()) }
    single { LanguageViewModel(get(), get()) }
    single { UserUpdateViewModel(get()) }
    factory { PrivacyViewModel(get()) }
    factory { NotificationsScreenModel(get()) }

    single { OnboardingScreenViewModel() }
    factory { PricingPlanScreenModel(get()) }

    single { LiveScreenViewModel(get()) }


    factory { LoginScreenViewModel(get(), get(), get()) }
    factory { RegisterNameViewModel(get()) }
    factory { RegisterPhoneViewModel(get(), get()) }
    factory { RegisterPasswordViewModel(get()) }
    factory { RegisterOtpScreenViewModel(get()) }
    factory { ForgotPasswordPhoneViewModel(get()) }
    factory { ForgetPasswordOtpViewModel(get()) }
    factory { ForgotPasswordChangePasswordViewModel(get(), get()) }
    factory { RegisterOtpScreenViewModel(get()) }
    factory { ProfileForgotPasswordPhoneViewModel(get()) }
    factory { ProfileForgetPasswordOtpViewModel(get()) }
    factory { ProfileForgotPasswordChangePasswordViewModel(get(), get()) }

    single { FullScreenStateModel() }


}

fun getDataModule(baseUrl: String) = module {

    single { ResponseHandler() }

    single<AuthService> { AuthServiceImpl(get(), baseUrl = baseUrl) }
    single<UserService> { UserServiceImpl(get(), baseUrl = baseUrl) }
    single<MoviesService> { MoviesServiceImpl(get(), baseUrl = baseUrl) }
    single<NotificationService> { NotificationServiceImpl(get(), baseUrl = baseUrl) }


    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }


    single { createJson() }
}

fun getUseCaseModule() = module {
    single { ValidatePhone() }
    single { ValidateName() }
    single { ValidatePassword() }
    single { ValidationPayment() }
    single { LanguageDataStore() }
}

fun createHttpClient(enableNetworkLogs: Boolean) = module {
    single {
        HttpClient(get()) {

            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }

            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { cause, _ -> println("exception $cause") }
            }


            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }


            if (enableNetworkLogs) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val provider: CachingManager = get()
                        val accessToken = provider.getAccessToken().firstOrNull().orEmpty()
                        BearerTokens(accessToken, accessToken)
                    }
                }
            }


//            if (enableNetworkLogs) {
//                install(InspektifyKtor) {
//                    logLevel = sp.bvantur.inspektify.ktor.LogLevel.All
//                    presentationType = PresentationType.AutoShake
//                }
//            }

            install(DefaultRequest) {
                val provider: LanguageDataStore = get()
                val language = provider.language.lowercase()
                header("X-localization", language)
            }
        }

    }
}

fun createJson() = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = true
    coerceInputValues = true
}
