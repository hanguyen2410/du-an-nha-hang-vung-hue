import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { HTTP_STATUS } from 'constants/global'

import cashierService from 'services/cashierService'

const namespace = 'payment'

const initialState = {
  data: {
    fees: 0,
    feesMoney: 0,
    discount: 0,
    discountMoney: 0,
    totalAmount: 0,
    receivedCashMoney: 0,
    receivedTransferMoney: 0,
    totalReceived: 0,
    changeMoney: 0,
  },
  status: HTTP_STATUS.IDLE,
  errorMessage: null,
  errorStatus: null,
}

export const payBill = createAsyncThunk(
  `${namespace}/payBill`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .payBill(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

const paymentSlice = createSlice({
  name: namespace,
  initialState,
  reducers: {
    setFees: (state, { payload }) => {
      const totalPrice = payload.totalPrice
      const fees = payload.fees
      const feesMoney = (totalPrice * fees) / 100
      state.data.fees = fees
      state.data.feesMoney = feesMoney
    },
    setDiscount: (state, { payload }) => {
      const totalPrice = payload.totalPrice
      const discount = payload.discount
      const discountMoney = (totalPrice * discount) / 100
      state.data.discount = discount
      state.data.discountMoney = discountMoney
    },
    setReceivedCashMoney: (state, { payload }) => {
      const receivedCashMoney = payload.receivedCashMoney
      state.data.receivedCashMoney = receivedCashMoney
    },
    setReceivedTransferMoney: (state, { payload }) => {
      const receivedTransferMoney = payload.receivedTransferMoney
      state.data.receivedTransferMoney = receivedTransferMoney
    },
    setChangeMoney: (state, { payload }) => {
      const receivedCashMoney = payload.receivedCashMoney
      const receivedTransferMoney = payload.receivedTransferMoney

      state.data.changeMoney =
        receivedCashMoney + receivedTransferMoney - state.data.totalAmount
    },
    initTotalAmount: (state, { payload }) => {
      state.data.totalAmount = payload
    },
    updateTotalAmount: (state, { payload }) => {
      const totalPrice = payload.totalPrice
      const totalAmount =
        totalPrice + state.data.feesMoney - state.data.discountMoney

      state.data.totalAmount = totalAmount
    },
    resetPaymentData: (state) => {
      state.data.fees = 0
      state.data.feesMoney = 0
      state.data.discount = 0
      state.data.discountMoney = 0
      state.data.totalAmount = 0
      state.data.receivedTransferMoney = 0
      state.data.receivedCashMoney = 0
      state.data.changeMoney = 0
    },
  },
  extraReducers(builder) {
    builder
      .addCase(payBill.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(payBill.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
      })
      .addCase(payBill.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED

        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })
  },
})

const { reducer, actions } = paymentSlice
export const {
  setFees,
  setDiscount,
  setReceivedCashMoney,
  setReceivedTransferMoney,
  setChangeMoney,
  initTotalAmount,
  updateTotalAmount,
  resetPaymentData,
} = actions

export default reducer
