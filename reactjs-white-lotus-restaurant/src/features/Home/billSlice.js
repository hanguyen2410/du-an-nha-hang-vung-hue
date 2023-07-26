import { createSlice } from '@reduxjs/toolkit'
import { HTTP_STATUS } from 'constants/global'

const namespace = 'bill'

const initialState = {
  data: {
    tempBillData: [],
  },
  status: HTTP_STATUS.IDLE,
  errorMessage: null,
  errorStatus: null,
}

const billSlice = createSlice({
  name: namespace,
  initialState,
  reducers: {
    updateTempBillData: (state, { payload }) => {
      state.data.tempBillData = payload
    },
  },
})

const { reducer, actions } = billSlice

export const { updateTempBillData } = actions

export default reducer
