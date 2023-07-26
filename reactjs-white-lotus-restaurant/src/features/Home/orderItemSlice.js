import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { HTTP_STATUS } from 'constants/global'

import cashierService from 'services/cashierService'

const namespace = 'orderItems'

const initialState = {
  data: {
    currentOrderItemsdata: [],
    updateOrderItemsdata: [],
    currentOrderItem: {},
    totalAmount: 0,
  },
  status: HTTP_STATUS.IDLE,
  errorMessage: null,
  errorStatus: null,
}

export const getOrderItemsByTableId = createAsyncThunk(
  `${namespace}/getOrderItemsByTableId`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .getOrderItemsByTableId(obj.tableId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const addItemKitchen = createAsyncThunk(
  `${namespace}/addItemKitchen`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .addItemKitchen(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const deleteItemStockOut = createAsyncThunk(
  `${namespace}/deleteItemStockOut`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .deleteItemStockOut(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const payBillAndChangeOrderItemsDONE = createAsyncThunk(
  `${namespace}/payBillAndChangeOrderItemsDONE`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .payBillAndChangeOrderItemsDONE(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const createBill = createAsyncThunk(
  `${namespace}/createBill`,
  async (orderId, { rejectWithValue }) => {
    return await cashierService
      .createBill(orderId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

const orderItemSlice = createSlice({
  name: namespace,
  initialState,
  reducers: {
    setCurrentOrderItems: (state, action) => {
      state.data.currentOrderItemsdata = action.payload
    },
    setUpdateOrderItems: (state, action) => {
      state.data.updateOrderItemsdata = action.payload
    },
    increaseQuantityOrderItem: (state, action) => {
      const product = action.payload

      const productIndex = state.data.updateOrderItemsdata.findIndex(
        (item) =>
          item.productId === product.productId && item.note === product.note
      )

      if (productIndex >= 0) {
        const currentQuantity =
          state.data.updateOrderItemsdata[productIndex].quantity

        const newQuantity = currentQuantity + 1
        const newAmount =
          newQuantity * state.data.updateOrderItemsdata[productIndex].price
        state.data.updateOrderItemsdata[productIndex].quantity = newQuantity
        state.data.updateOrderItemsdata[productIndex].amount = newAmount
      }
    },
    decreaseQuantityOrderItem: (state, action) => {
      const product = action.payload

      const productIndex = state.data.updateOrderItemsdata.findIndex(
        (item) =>
          item.productId === product.productId && item.note === product.note
      )

      if (productIndex >= 0) {
        const currentQuantity =
          state.data.updateOrderItemsdata[productIndex].quantity

        const newQuantity = currentQuantity - 1
        const newAmount =
          newQuantity * state.data.updateOrderItemsdata[productIndex].price
        state.data.updateOrderItemsdata[productIndex].quantity = newQuantity
        state.data.updateOrderItemsdata[productIndex].amount = newAmount
      }
    },
    increaseQuantityCurrentOrderItem: (state, action) => {
      const newQuantity = state.data.currentOrderItem.quantity + 1
      const newAmount = newQuantity * state.data.currentOrderItem.price
      state.data.currentOrderItem.quantity = newQuantity
      state.data.currentOrderItem.amount = newAmount
    },
    decreaseQuantityCurrentOrderItem: (state, action) => {
      const newQuantity = state.data.currentOrderItem.quantity - 1
      const newAmount = newQuantity * state.data.currentOrderItem.price
      state.data.currentOrderItem.quantity = newQuantity
      state.data.currentOrderItem.amount = newAmount
    },
    changeQuantityCurrentOrderItem: (state, action) => {
      const newQuantity = action.payload
      const newAmount = newQuantity * state.data.currentOrderItem.price
      state.data.currentOrderItem.quantity = newQuantity
      state.data.currentOrderItem.amount = newAmount
    },
    setNoteCurrentOrderItem: (state, action) => {
      state.data.currentOrderItem.note = action.payload
    },
    setCurrentOrderItem: (state, action) => {
      state.data.currentOrderItem = action.payload
    },
    resetCurrentOrderItems: (state, action) => {
      state.data.currentOrderItemsdata = []
    },
    resetUpdateOrderItems: (state, action) => {
      state.data.updateOrderItemsdata = []
    },
    resetOrderItems: (state, action) => {
      state.data.currentOrderItemsdata = []
      state.data.updateOrderItemsdata = []
      state.data.totalAmount = 0
    },
    setTotalAmount: (state, action) => {
      let totalAmount = 0
      state.data.currentOrderItemsdata.forEach((item) => {
        totalAmount += item.amount
      })
      state.data.updateOrderItemsdata.forEach((item) => {
        totalAmount += item.amount
      })
      state.data.totalAmount = totalAmount
    },
    addItem: (state, action) => {
      if (state.data.updateOrderItemsdata.length === 0) {
        state.data.updateOrderItemsdata.push(state.data.currentOrderItem)
      } else {
        const productsFilter = state.data.updateOrderItemsdata.filter(
          (item) =>
            item.productId === state.data.currentOrderItem.productId &&
            item.note === state.data.currentOrderItem.note
        )

        if (productsFilter.length > 0) {
          const productIndex = state.data.updateOrderItemsdata.findIndex(
            (item) =>
              item.productId === state.data.currentOrderItem.productId &&
              item.note === state.data.currentOrderItem.note
          )

          const newQuantity =
            productsFilter[0].quantity + state.data.currentOrderItem.quantity
          const newAmount = state.data.currentOrderItem.price * newQuantity

          state.data.updateOrderItemsdata[productIndex].quantity = newQuantity
          state.data.updateOrderItemsdata[productIndex].amount = newAmount
        } else {
          state.data.updateOrderItemsdata.push(state.data.currentOrderItem)
        }
      }

      state.data.currentOrderItem = {}
    },
    deleteUpdateOrderItem: (state, action) => {
      let indexInOrderItems = action.payload
      let indexUpdateOrderItemsdata =
        indexInOrderItems - state.data.currentOrderItemsdata.length
      state.data.updateOrderItemsdata.splice(indexUpdateOrderItemsdata, 1)
    },
    updateItem: (state, action) => {
      if (state.data.updateOrderItemsdata.length === 0) {
        state.data.updateOrderItemsdata.push(state.data.currentOrderItem)
      } else {
        const productsFilter = state.data.updateOrderItemsdata.filter(
          (item) =>
            item.productId === state.data.currentOrderItem.productId &&
            item.note === state.data.currentOrderItem.note
        )

        if (productsFilter.length > 0) {
          const productIndex = state.data.updateOrderItemsdata.findIndex(
            (item) =>
              item.productId === state.data.currentOrderItem.productId &&
              item.note === state.data.currentOrderItem.note
          )

          const newQuantity = state.data.currentOrderItem.quantity
          const newAmount = state.data.currentOrderItem.price * newQuantity

          state.data.updateOrderItemsdata[productIndex].quantity = newQuantity
          state.data.updateOrderItemsdata[productIndex].amount = newAmount
        } else {
          state.data.updateOrderItemsdata.push(state.data.currentOrderItem)
        }
      }

      state.data.currentOrderItem = {}
    },
  },
  extraReducers(builder) {
    // const setTableAddItemStatusAction = setCurrentTableAddItemStatus(
    //   TABLE_STATUS.UPDATE
    // )
    // dispatch(setTableAddItemStatusAction)

    // const setCurrentTableOrderIdAction = setCurrentTableOrderId(
    //   orderTableRes.orderId
    // )
    // dispatch(setCurrentTableOrderIdAction)

    // dispatch(setLoadingFalse())

    // const action = setCurrentOrderItems(orderTableRes.orderItems)
    // dispatch(action)
    builder
      .addCase(getOrderItemsByTableId.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(getOrderItemsByTableId.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        const orderItems = payload.orderItems
        state.data.currentOrderItemsdata = orderItems
        state.data.updateOrderItemsdata = []
      })
      .addCase(getOrderItemsByTableId.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED

        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })

      // .addCase(addItemKitchen.pending, (state) => {
      //   state.status = HTTP_STATUS.PENDING
      // })
      // .addCase(addItemKitchen.fulfilled, (state, { payload }) => {
      //   state.status = HTTP_STATUS.FULFILLED

      //   const orderItems = payload.orderItems
      //   state.data.currentOrderItemsdata = orderItems
      //   state.data.updateOrderItemsdata = []
      // })
      // .addCase(addItemKitchen.rejected, (state, { payload }) => {
      //   state.status = HTTP_STATUS.REJECTED

      //   if (payload.response) {
      //     state.errorMessage = payload.response.statusText
      //     state.errorStatus = payload.response.status
      //   }
      // })

      .addCase(deleteItemStockOut.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(deleteItemStockOut.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        const orderItemId = payload.orderItemId

        const orderItemIndex = state.data.currentOrderItemsdata.findIndex(
          (item) => item.id === orderItemId
        )

        if (orderItemIndex >= 0) {
          state.data.currentOrderItemsdata.splice(orderItemIndex, 1)
        }
      })
      .addCase(deleteItemStockOut.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED

        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })

      .addCase(payBillAndChangeOrderItemsDONE.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(
        payBillAndChangeOrderItemsDONE.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED
        }
      )
      .addCase(
        payBillAndChangeOrderItemsDONE.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED

          if (payload.response) {
            state.errorMessage = payload.response.statusText
            state.errorStatus = payload.response.status
          }
        }
      )

      .addCase(createBill.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(createBill.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
      })
      .addCase(createBill.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED

        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })
  },
})

const { reducer, actions } = orderItemSlice
export const {
  setCurrentOrderItems,
  setUpdateOrderItems,
  increaseQuantityOrderItem,
  decreaseQuantityOrderItem,
  increaseQuantityCurrentOrderItem,
  decreaseQuantityCurrentOrderItem,
  changeQuantityCurrentOrderItem,
  setNoteCurrentOrderItem,
  setCurrentOrderItem,
  resetCurrentOrderItems,
  resetUpdateOrderItems,
  resetCurrentOrderItem,
  resetOrderItems,
  setTotalAmount,
  addItem,
  deleteUpdateOrderItem,
  updateItem,
} = actions

export default reducer
