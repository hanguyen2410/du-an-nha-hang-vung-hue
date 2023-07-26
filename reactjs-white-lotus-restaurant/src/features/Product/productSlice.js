import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { HTTP_STATUS } from 'constants/global'

import productService from 'services/productService'

const namespace = 'products'

const initialState = {
  data: {
    products: [],
    categories: [],
    tables: [],
  },
  status: HTTP_STATUS.IDLE,
  errorMessage: null,
  errorStatus: null,
}

export const fetchProductsData = createAsyncThunk(
  `${namespace}/fetchProductsData`,
  async (obj, { rejectWithValue }) => {
    return await productService
      .getAlls()
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

const productSlice = createSlice({
  name: namespace,
  initialState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(fetchProductsData.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(fetchProductsData.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
        state.data.products = payload
      })
      .addCase(fetchProductsData.rejected, (state, { payload }) => {
        state.status = 'failed'
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })
  },
})

const { reducer } = productSlice

export default reducer
