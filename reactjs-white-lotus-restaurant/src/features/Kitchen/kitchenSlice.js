import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { HTTP_STATUS } from 'constants/global'

import kitchenService from 'services/kitchenService'

const namespace = 'kitchen'

const initialState = {
  data: {
    orderItemsCooking: [],
    orderItemsTable: [],
    orderItemsWaiter: [],
    orderItemsDelivery: [],
  },
  status: HTTP_STATUS.IDLE,
  errorMessage: null,
  errorStatus: null,
}

export const fetchAllKitchenData = createAsyncThunk(
  `${namespace}/fetchAllKitchenData`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .getAllData()
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const fetchCookingKitchenData = createAsyncThunk(
  `${namespace}/fetchCookingKitchenData`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .getListItemsCooking()
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const fetchWaiterKitchenData = createAsyncThunk(
  `${namespace}/fetchWaiterKitchenData`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .getListItemsWaiter()
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeCookingToWaiterOneProductOfProductGroup = createAsyncThunk(
  `${namespace}/changeCookingToWaiterOneProductOfProductGroup`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeCookingToWaiterOneProductOfProductGroup(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeCookingToWaiterAllProductOfProductGroup = createAsyncThunk(
  `${namespace}/changeCookingToWaiterAllProductOfProductGroup`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeCookingToWaiterAllProductOfProductGroup(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeCookingToWaiterOneProductOfTableGroup = createAsyncThunk(
  `${namespace}/changeCookingToWaiterOneProductOfTableGroup`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeCookingToWaiterOneProductOfTableGroup(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeCookingToWaiterToProductOfTableGroup = createAsyncThunk(
  `${namespace}/changeCookingToWaiterToProductOfTableGroup`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeCookingToWaiterToProductOfTableGroup(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeWaiterToDelivery = createAsyncThunk(
  `${namespace}/changeWaiterToDelivery`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeWaiterToDelivery(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeWaiterToDeliveryAllProductOfProductGroup = createAsyncThunk(
  `${namespace}/changeWaiterToDeliveryAllProductOfProductGroup`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeWaiterToDeliveryAllProductOfProductGroup(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeCookingToStockOutOneProductOfTableGroup = createAsyncThunk(
  `${namespace}/changeCookingToStockOutOneProductOfTableGroup`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeCookingToStockOutOneProductOfTableGroup(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeCookingToStockOutToProductOfTableGroup = createAsyncThunk(
  `${namespace}/changeCookingToStockOutToProductOfTableGroup`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeCookingToStockOutToProductOfTableGroup(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeWaiterToStockOut = createAsyncThunk(
  `${namespace}/changeWaiterToStockOut`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeWaiterToStockOut(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeWaiterToStockOutToProductOfOrder = createAsyncThunk(
  `${namespace}/changeWaiterToStockOutToProductOfOrder`,
  async (obj, { rejectWithValue }) => {
    return await kitchenService
      .changeWaiterToStockOutToProductOfOrder(obj.orderItemId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeStatusFromCookingToWaiterAllProductOfTable =
  createAsyncThunk(
    `${namespace}/changeStatusFromCookingToWaiterAllProductOfTable`,
    async (obj, { rejectWithValue }) => {
      return await kitchenService
        .changeStatusFromCookingToWaiterAllProductOfTable(obj.orderId)
        .then((response) => {
          return response.data
        })
        .catch((error) => {
          return rejectWithValue(error)
        })
    }
  )

const kitchenSlice = createSlice({
  name: namespace,
  initialState,
  reducers: {
    setKitchenDataLoading: (state, { payload }) => {
      state.data.loading = payload
    },
    updateOrderItems: (state, { payload }) => {
      state.data.orderItemsCooking = payload.itemsCooking
      state.data.orderItemsTable = payload.itemsTable
      state.data.orderItemsWaiter = payload.itemsWaiter
    },
    updateKitchenData: (state, { payload }) => {
      state.data.orderItemsCooking = payload.itemsCooking
      state.data.orderItemsTable = payload.itemsTable
      state.data.orderItemsWaiter = payload.itemsWaiter
    },
  },
  extraReducers(builder) {
    builder
      .addCase(fetchAllKitchenData.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(fetchAllKitchenData.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        state.data.orderItemsCooking = payload.itemsCooking
        state.data.orderItemsTable = payload.itemsTable
        state.data.orderItemsWaiter = payload.itemsWaiter
      })
      .addCase(fetchAllKitchenData.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })

      .addCase(fetchCookingKitchenData.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(fetchCookingKitchenData.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
        state.data.orderItemsCooking = payload
      })
      .addCase(fetchCookingKitchenData.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })

      .addCase(fetchWaiterKitchenData.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(fetchWaiterKitchenData.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
        state.data.orderItemsWaiter = payload
      })
      .addCase(fetchWaiterKitchenData.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })

      .addCase(
        changeCookingToWaiterOneProductOfProductGroup.pending,
        (state) => {
          state.status = HTTP_STATUS.PENDING
        }
      )
      .addCase(
        changeCookingToWaiterOneProductOfProductGroup.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          const newItem = payload

          // Cập nhật danh sách chờ phục vụ
          const itemWaiterIndex = state.data.orderItemsWaiter.findIndex(
            (item) =>
              item.orderItemId === newItem.orderItemId &&
              item.tableId === newItem.tableId &&
              item.productId === newItem.productId &&
              item.note === newItem.note
          )

          if (itemWaiterIndex >= 0) {
            state.data.orderItemsWaiter[itemWaiterIndex] = newItem
          } else {
            state.data.orderItemsWaiter.push(newItem)
          }

          // Cập nhật danh sách đang nấu theo sản phẩm
          const itemCookingIndex = state.data.orderItemsCooking.findIndex(
            (item) =>
              item.productId === newItem.productId && item.note === newItem.note
          )

          if (itemCookingIndex >= 0) {
            if (state.data.orderItemsCooking[itemCookingIndex].quantity === 1) {
              state.data.orderItemsCooking.splice(itemCookingIndex, 1)
            } else {
              const newQuantity =
                state.data.orderItemsCooking[itemCookingIndex].quantity - 1
              state.data.orderItemsCooking[itemCookingIndex].quantity =
                newQuantity
            }
          }

          // Cập nhật danh sách đang nấu theo bàn
          const itemTableIndex = state.data.orderItemsTable.findIndex(
            (item) => item.tableId === newItem.tableId
          )

          if (itemTableIndex >= 0) {
            const countProduct =
              state.data.orderItemsTable[itemTableIndex].countProduct

            if (countProduct === 1) {
              state.data.orderItemsTable.splice(itemTableIndex, 1)
            } else {
              const orderItems =
                state.data.orderItemsTable[itemTableIndex].orderItems

              state.data.orderItemsTable[itemTableIndex].countProduct =
                countProduct - 1

              const itemIndex = orderItems.findIndex(
                (item) =>
                  item.productId === newItem.productId &&
                  item.note === newItem.note
              )

              if (itemIndex >= 0) {
                const currentQuantity =
                  state.data.orderItemsTable[itemTableIndex].orderItems[
                    itemIndex
                  ].quantity
                if (currentQuantity === 1) {
                  state.data.orderItemsTable[itemTableIndex].orderItems.splice(
                    itemIndex,
                    1
                  )
                } else {
                  const newQuantity = currentQuantity - 1
                  state.data.orderItemsTable[itemTableIndex].orderItems[
                    itemIndex
                  ].quantity = newQuantity
                }
              }
            }
          }
        }
      )
      .addCase(
        changeCookingToWaiterOneProductOfProductGroup.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )

      .addCase(
        changeCookingToWaiterAllProductOfProductGroup.pending,
        (state) => {
          state.status = HTTP_STATUS.PENDING
        }
      )
      .addCase(
        changeCookingToWaiterAllProductOfProductGroup.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          state.data.orderItemsCooking = payload.itemsCooking
          state.data.orderItemsTable = payload.itemsTable
          state.data.orderItemsWaiter = payload.itemsWaiter
        }
      )
      .addCase(
        changeCookingToWaiterAllProductOfProductGroup.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )

      .addCase(changeCookingToWaiterOneProductOfTableGroup.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(
        changeCookingToWaiterOneProductOfTableGroup.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          const newItem = payload

          // Cập nhật danh sách chờ phục vụ
          const itemWaiterIndex = state.data.orderItemsWaiter.findIndex(
            (item) =>
              item.orderItemId === newItem.orderItemId &&
              item.tableId === newItem.tableId &&
              item.productId === newItem.productId &&
              item.note === newItem.note
          )

          if (itemWaiterIndex >= 0) {
            state.data.orderItemsWaiter[itemWaiterIndex] = newItem
          } else {
            state.data.orderItemsWaiter.push(newItem)
          }

          // Cập nhật danh sách đang nấu theo sản phẩm
          const itemCookingIndex = state.data.orderItemsCooking.findIndex(
            (item) =>
              item.productId === newItem.productId && item.note === newItem.note
          )

          if (itemCookingIndex >= 0) {
            if (state.data.orderItemsCooking[itemCookingIndex].quantity === 1) {
              state.data.orderItemsCooking.splice(itemCookingIndex, 1)
            } else {
              const newQuantity =
                state.data.orderItemsCooking[itemCookingIndex].quantity - 1
              state.data.orderItemsCooking[itemCookingIndex].quantity =
                newQuantity
            }
          }

          // Cập nhật danh sách đang nấu theo bàn
          const itemTableIndex = state.data.orderItemsTable.findIndex(
            (item) => item.tableId === newItem.tableId
          )

          if (itemTableIndex >= 0) {
            const currentCountProduct =
              state.data.orderItemsTable[itemTableIndex].countProduct

            if (currentCountProduct === 1) {
              state.data.orderItemsTable.splice(itemTableIndex, 1)
            } else {
              state.data.orderItemsTable[itemTableIndex].countProduct =
                currentCountProduct - 1

              const orderItems =
                state.data.orderItemsTable[itemTableIndex].orderItems

              const itemIndex = orderItems.findIndex(
                (item) =>
                  item.productId === newItem.productId &&
                  item.note === newItem.note
              )

              if (itemIndex >= 0) {
                const currentQuantity =
                  state.data.orderItemsTable[itemTableIndex].orderItems[
                    itemIndex
                  ].quantity
                if (currentQuantity === 1) {
                  state.data.orderItemsTable[itemTableIndex].orderItems.splice(
                    itemIndex,
                    1
                  )
                } else {
                  const newQuantity = currentQuantity - 1
                  state.data.orderItemsTable[itemTableIndex].orderItems[
                    itemIndex
                  ].quantity = newQuantity
                }
              }
            }
          }
        }
      )
      .addCase(
        changeCookingToWaiterOneProductOfTableGroup.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )

      .addCase(changeCookingToWaiterToProductOfTableGroup.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(
        changeCookingToWaiterToProductOfTableGroup.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          state.data.orderItemsCooking = payload.itemsCooking
          state.data.orderItemsTable = payload.itemsTable
          state.data.orderItemsWaiter = payload.itemsWaiter
        }
      )
      .addCase(
        changeCookingToWaiterToProductOfTableGroup.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )

      .addCase(changeWaiterToDelivery.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(changeWaiterToDelivery.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        state.data.orderItemsWaiter = payload
        // state.data.orderItemsCooking = payload.itemsCooking
        // state.data.orderItemsTable = payload.itemsTable
        // state.data.orderItemsWaiter = payload.itemsWaiter
      })
      .addCase(changeWaiterToDelivery.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })

      .addCase(
        changeCookingToStockOutOneProductOfTableGroup.pending,
        (state) => {
          state.status = HTTP_STATUS.PENDING
        }
      )
      .addCase(
        changeCookingToStockOutOneProductOfTableGroup.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          // state.data.orderItemsCooking = payload.itemsCooking
          // state.data.orderItemsTable = payload.itemsTable

          const newItem = payload

          // Cập nhật danh sách đang nấu theo sản phẩm
          const itemCookingIndex = state.data.orderItemsCooking.findIndex(
            (item) =>
              item.productId === newItem.productId && item.note === newItem.note
          )

          if (itemCookingIndex >= 0) {
            if (state.data.orderItemsCooking[itemCookingIndex].quantity === 1) {
              state.data.orderItemsCooking.splice(itemCookingIndex, 1)
            } else {
              const newQuantity =
                state.data.orderItemsCooking[itemCookingIndex].quantity - 1
              state.data.orderItemsCooking[itemCookingIndex].quantity =
                newQuantity
            }
          }

          // Cập nhật danh sách đang nấu theo bàn
          const itemTableIndex = state.data.orderItemsTable.findIndex(
            (item) => item.tableId === newItem.tableId
          )

          if (itemTableIndex >= 0) {
            const countProduct =
              state.data.orderItemsTable[itemTableIndex].countProduct

            if (countProduct === 1) {
              state.data.orderItemsTable.splice(itemTableIndex, 1)
            } else {
              const orderItems =
                state.data.orderItemsTable[itemTableIndex].orderItems

              state.data.orderItemsTable[itemTableIndex].countProduct =
                countProduct - 1

              const itemIndex = orderItems.findIndex(
                (item) =>
                  item.productId === newItem.productId &&
                  item.note === newItem.note
              )

              if (itemIndex >= 0) {
                const currentQuantity =
                  state.data.orderItemsTable[itemTableIndex].orderItems[
                    itemIndex
                  ].quantity
                if (currentQuantity === 1) {
                  state.data.orderItemsTable[itemTableIndex].orderItems.splice(
                    itemIndex,
                    1
                  )
                } else {
                  const newQuantity = currentQuantity - 1
                  state.data.orderItemsTable[itemTableIndex].orderItems[
                    itemIndex
                  ].quantity = newQuantity
                }
              }
            }
          }
        }
      )
      .addCase(
        changeCookingToStockOutOneProductOfTableGroup.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )

      .addCase(
        changeCookingToStockOutToProductOfTableGroup.pending,
        (state) => {
          state.status = HTTP_STATUS.PENDING
        }
      )
      .addCase(
        changeCookingToStockOutToProductOfTableGroup.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          state.data.orderItemsCooking = payload.itemsCooking
          state.data.orderItemsTable = payload.itemsTable
        }
      )
      .addCase(
        changeCookingToStockOutToProductOfTableGroup.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )

      .addCase(changeWaiterToStockOut.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(changeWaiterToStockOut.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        state.data.orderItemsWaiter = payload
      })
      .addCase(changeWaiterToStockOut.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })

      .addCase(changeWaiterToStockOutToProductOfOrder.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(
        changeWaiterToStockOutToProductOfOrder.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          state.data.orderItemsWaiter = payload
        }
      )
      .addCase(
        changeWaiterToStockOutToProductOfOrder.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )

      .addCase(
        changeWaiterToDeliveryAllProductOfProductGroup.pending,
        (state) => {
          state.status = HTTP_STATUS.PENDING
        }
      )
      .addCase(
        changeWaiterToDeliveryAllProductOfProductGroup.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          state.data.orderItemsWaiter = payload
          // state.data.orderItemsCooking = payload.itemsCooking
          // state.data.orderItemsTable = payload.itemsTable
          // state.data.orderItemsWaiter = payload.itemsWaiter
        }
      )
      .addCase(
        changeWaiterToDeliveryAllProductOfProductGroup.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )
      .addCase(
        changeStatusFromCookingToWaiterAllProductOfTable.pending,
        (state) => {
          state.status = HTTP_STATUS.PENDING
        }
      )
      .addCase(
        changeStatusFromCookingToWaiterAllProductOfTable.fulfilled,
        (state, { payload }) => {
          state.status = HTTP_STATUS.FULFILLED

          state.data.orderItemsCooking = payload.itemsCooking
          state.data.orderItemsTable = payload.itemsTable
          state.data.orderItemsWaiter = payload.itemsWaiter
        }
      )
      .addCase(
        changeStatusFromCookingToWaiterAllProductOfTable.rejected,
        (state, { payload }) => {
          state.status = HTTP_STATUS.REJECTED
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      )
  },
})

const { reducer, actions } = kitchenSlice
export const { setKitchenDataLoading, updateOrderItems, updateKitchenData } =
  actions

export default reducer
