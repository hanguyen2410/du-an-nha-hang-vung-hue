import { configureStore } from '@reduxjs/toolkit'
import restaurantReducer from 'features/Home/restaurantSlice'
import kitchenReducer from 'features/Kitchen/kitchenSlice'
import orderItemReducer from 'features/Home/orderItemSlice'
import paymentReducer from 'features/Home/paymentSlice'
import billReducer from 'features/Home/billSlice'
import authReducer from 'features/Login/authSlice'

const rootReducer = {
  baseData: restaurantReducer,
  kitchenData: kitchenReducer,
  orderItemData: orderItemReducer,
  paymentData: paymentReducer,
  billData: billReducer,
  authData: authReducer,
}

const store = configureStore({
  reducer: rootReducer,
})

export default store
