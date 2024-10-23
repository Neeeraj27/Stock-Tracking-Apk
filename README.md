# Stock Tracking Application

## Overview
I created this application to provide real-time stock price updates and historical data for **US-based companies**. Users can log in, explore stocks, and view interactive charts showing price trends for various timeframes.

## Key Features
- **User Authentication**: After signing up, user details are stored in **MockAPI** ([Link to MockAPI](https://mockapi.io/projects/6712ba8a6c5f5ced66247e85)).
- **Real-Time and Historical Stock Data**: I initially planned to use **WebSockets** for real-time data but shifted to the **Alpha Vantage API** due to limitations with premium access for WebSocket services. Mock data is shown when the API daily limit is reached.
- **Shared Preferences**: I use shared preferences to manage user sessions and store small amounts of data.
- **Dynamic Stock Charts**: Users can explore stock trends using **TradingView** for real-time charts, and view historical data for intervals such as 1D, 1W, 1M, and more.
- **Animations**: I added **Lottie** animations to enhance the experience, showing loading and empty states.

## Libraries and Tools I Used
- **Retrofit**: For making API requests.
- **Coil**: For dynamic image loading (stock logos).
- **Lottie**: For adding animations to indicate different states.
- **TradingView**: For interactive stock charting.
- **Alpha Vantage API**: To fetch real-time stock data (limited to 500 requests/day).
- **MockAPI**: Used for mock data storage when API limits are exceeded.
- **Jetpack Compose**: For building a modern and responsive UI.

## How to Use the App
1. **Sign Up & Login**: After signing up, your data is stored in **MockAPI**. 
2. **Explore Stocks**: You can view stock prices for **US-based companies** such as Apple, Google, and Tesla. Clicking on any stock shows a detailed price history and an interactive chart.
3. **Select Timeframe**: Choose from intervals like **1D**, **1W**, **1M**, **1Y**, or **5Y** to view historical data.
4. **Empty States**: If you have no holdings or watchlist items, custom animations will guide you through empty states.

## Challenges I Faced
- **WebSocket Integration**: My original plan was to use **WebSocket** for real-time stock data updates. However, due to the premium subscriptions required for WebSocket APIs, I switched to **Alpha Vantage API**.
- **Daily API Limits**: Since the **Alpha Vantage API** has a limit of 500 requests per day, I implemented mock data when the limit is reached. However, the charts still show accurate data for the stock selected.
- **Push Notifications**: I initially wanted to include push notifications for stock changes but decided to drop the feature after switching to a normal API, as it was no longer critical.

## Future Enhancements
- Implementing push notifications for stock changes.
- Expanding stock listings beyond **US-based** companies.

## How to Run the App
1. Clone the repository from GitHub.
2. Open the project in Android Studio.
3. Make sure you have the necessary API keys for **Alpha Vantage API**.
4. Run the app on an emulator or a physical device.
