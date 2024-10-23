# Stock Tracking Application

## Overview
I built this application to offer users real-time stock price updates for **US-based companies** and display historical data using dynamic charts. The app uses **MVVM architecture** for better code management and **Dependency Injection** via Hilt to keep components decoupled and maintainable.

## Key Features
- **User Authentication**: Upon signup, user data is stored in **MockAPI** ([MockAPI Project](https://mockapi.io/projects/6712ba8a6c5f5ced66247e85)).
- **Real-Time and Historical Stock Data**: My initial priority was to use **WebSockets** for real-time updates. However, due to limitations with free WebSocket APIs, I switched to **Alpha Vantage API**, which provides historical stock data with a daily request limit. To handle this, I use **mock data** after hitting the API limit.
- **MVVM Architecture**: The app is structured around **Model-View-ViewModel (MVVM)**, which allows me to separate the business logic, UI, and data layers effectively.
- **Dependency Injection (DI)**: I used **Hilt** for DI, ensuring cleaner code and better testability.
- **Shared Preferences**: I also use **Shared Preferences** to manage user sessions and handle persistent local storage for small amounts of data.
- **Stock Charts**: Stock charts are displayed using **TradingView** with customizable timeframes (e.g., 1D, 1W, 1M, 1Y, 5Y), offering users an interactive way to explore stock trends.
- **Animations**: Lottie animations are used to show loading and empty states, enhancing the user experience.

## Libraries and Tools I Used
- **Retrofit**: For seamless API requests.
- **Coil**: For dynamic image loading (stock logos).
- **Lottie**: For animations to signal loading states or empty watchlists/holdings.
- **TradingView**: For interactive stock charting.
- **Alpha Vantage API**: To fetch real-time and historical stock data (limited to 20 requests/day).
- **MockAPI**: For storing mock data when API limits are reached.
- **Jetpack Compose**: To build the UI with a modern declarative approach.
- **Hilt**: For dependency injection to simplify code and improve testability.

## How to Use the App
1. **Sign Up & Login**: After signing up, your information is stored in **MockAPI**. Shared preferences are used to keep track of your session.
2. **Explore Stocks**: You can view stock prices for **US-based companies** like Apple, Google, and Tesla. When you click on any stock, the app displays detailed price history in a chart.
3. **View Historical Data**: The app allows you to switch between different timeframes—1D, 1W, 1M, 1Y, and 5Y—via the charting interface powered by **TradingView**.
4. **Animations**: When data is loading or unavailable, animations will guide you, providing a smooth user experience.

## Challenges I Encountered
- **WebSocket Integration**: My first priority was to implement real-time stock data via **WebSocket**. I managed to establish WebSocket connections, but unfortunately, most of the WebSocket services required premium subscriptions for historical data. Due to this, I shifted to using **Alpha Vantage API**.
- **API Limits**: Since the **Alpha Vantage API** only allows 20 requests per day, I incorporated mock data to ensure that the app remains functional even after hitting the daily limit. However, the charts still show accurate data from the most recent API call.
- **Push Notifications**: I originally intended to include push notifications to alert users of stock price changes in real-time, but once I switched from WebSocket to regular API calls, I decided not to implement it, as it became less critical.

## Future Enhancements
- **Push Notifications**: I plan to implement push notifications for real-time stock changes in the future.
- **Expanded Stock Listings**: I may expand the app to include non-US-based stocks in future versions.

## How to Run the App
1. Clone the repository from GitHub.
2. Open the project in **Android Studio**.
3. Ensure you have the required **Alpha Vantage API** keys.
4. Run the app on an emulator or a physical device.

## Screenshots
