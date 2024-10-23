# Stock Tracking Application

## Overview
This application provides real-time stock price updates and historical data for **US-based companies**. Users can log in, explore stocks, and view interactive charts showing price trends.

## Key Features
- **User Authentication**: Users can sign up and log in. Data such as user details and session information are stored in **MockAPI** ([Link to MockAPI](https://mockapi.io/projects/6712ba8a6c5f5ced66247e85)).
- **Real-Time and Historical Stock Data**: Initially planned with **WebSockets**, but due to limitations (such as premium access for full features), **Alpha Vantage API** was used to fetch stock data. When API limits are reached, mock data is displayed.
- **Shared Preferences**: Manages user sessions.
- **Dynamic Stock Charts**: Powered by **TradingView**, users can explore real-time stock trends, as well as historical data for intervals like 1D, 1W, 1M, and more.
- **Animations**: **Lottie** animations for empty states (holdings, watchlist) and loading.

## Libraries and Tools
- **Retrofit**: For API requests
- **Coil**: For dynamic image loading (stock logos)
- **Lottie**: For animated feedback
- **TradingView**: For interactive charting
- **Alpha Vantage API**: To fetch real-time stock data (limited to 500 requests/day)
- **MockAPI**: Stores mock data when API limits are exceeded
- **Jetpack Compose**: For building a modern and responsive UI

## How to Use
1. **Sign Up & Login**: After registering, user data is stored in **MockAPI**.
2. **Explore Stocks**: View stock prices for **US-based companies** like Apple, Google, and Tesla. Click on a stock to view detailed price history and interactive charts.
3. **Select Timeframe**: Choose from intervals such as **1D**, **1W**, **1M**, **1Y**, or **5Y** to display historical data.
4. **Empty States**: If you have no holdings or watchlist items, appropriate animations will guide you.

## Challenges
- **WebSocket Integration**: Initially, the plan was to use **WebSocket** for real-time updates. However, most available services required premium subscriptions to access historical data, so we shifted to **Alpha Vantage API**.
- **Daily API Limits**: Due to daily API request limits, mock data is displayed when the limit is reached, though stock charts still show accurate historical data.
- **Push Notifications**: Initially, push notifications were planned to notify users of stock changes. However, after switching to a normal API, this feature was dropped as it was less critical.

## Future Enhancements
- Implementing push notifications for stock changes.
- Expanding stock listings beyond **US-based** companies.
