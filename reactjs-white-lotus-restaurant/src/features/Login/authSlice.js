import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { HTTP_STATUS } from 'constants/global'
import jwtDecode from 'jwt-decode'
import authService from 'services/authService'

const namespace = 'auth'

const initialState = {
  data: {},
  status: HTTP_STATUS.IDLE,
  errorMessage: null,
  errorStatus: null,
}

export const login = createAsyncThunk(
  `${namespace}/login`,
  async (obj, { rejectWithValue }) => {
    return await authService
      .login(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

const authSlice = createSlice({
  name: namespace,
  initialState,
  reducers: {
    setAuth: (state, { payload }) => {
      state.data = payload
    },
    clearData: (state) => {
      state.data = {}
    },
  },
  extraReducers(builder) {
    builder
      .addCase(login.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(login.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        const tokenData = jwtDecode(payload.token)

        state.data = tokenData
      })
      .addCase(login.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })
  },
})

const { reducer, actions } = authSlice
export const { setAuth, clearData } = actions

export default reducer
