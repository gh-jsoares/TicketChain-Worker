package ticketchain.mobile.worker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ticketchain.mobile.worker.api.TicketChainApi
import ticketchain.mobile.worker.services.AccountService
import ticketchain.mobile.worker.state.AppState
import ticketchain.mobile.worker.storage.UserDataService
import ticketchain.mobile.worker.storage.userDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun getAccountService(
        api: TicketChainApi,
        userDataService: UserDataService,
        state: AppState
    ): AccountService =
        AccountService(api, userDataService, state)

    @Singleton
    @Provides
    fun getAppState(): AppState = AppState()

    @Singleton
    @Provides
    fun getUserDataService(@ApplicationContext context: Context): UserDataService =
        UserDataService(context.userDataStore)

    @Singleton
    @Provides
    fun getApi(store: AppState): TicketChainApi = TicketChainApi(store)
}