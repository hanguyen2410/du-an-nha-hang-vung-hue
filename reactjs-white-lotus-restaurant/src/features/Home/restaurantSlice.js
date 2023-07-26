import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import { EnumStatus } from 'constants/EnumStatus'
import { HTTP_STATUS } from 'constants/global'

import restaurantService from 'services/restaurantService'

import productService from 'services/productService'

import cashierService from 'services/cashierService'

const namespace = 'restaurant'

const initialState = {
  data: {
    products: [],
    productsSearch: [],
    categories: [],
    tables: [],
    listBillToday: [],
    listBillPreviousDay: [],
    listBillShow: [],
    currentBill: {},
    currenrOrderitemsBill: [],
    currentTable: {},
    billsOfDay: [],
    loading: false,
  },
  status: HTTP_STATUS.IDLE,
  errorMessage: null,
  errorStatus: null,
}

export const fetchRestaurantsData = createAsyncThunk(
  `${namespace}/fetchRestaurantsData`,
  async (obj, { rejectWithValue }) => {
    return await restaurantService
      .getAlls()
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const fetchTablesAndCategoriesData = createAsyncThunk(
  `${namespace}/fetchTablesAndCategoriesData`,
  async (obj, { rejectWithValue }) => {
    return await restaurantService
      .getAllTablesAndCategories()
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const fetchProductsData = createAsyncThunk(
  `${namespace}/fetchProductsData`,
  async (obj, { rejectWithValue }) => {
    return await restaurantService
      .getAllProducts()
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const fetchChangeTableStatus = createAsyncThunk(
  `${namespace}/fetchChangeTableStatus`,
  async (obj, { rejectWithValue }) => {
    return await restaurantService
      .changeStatusTable(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const changeTable = createAsyncThunk(
  `${namespace}/changeTable`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .changeTable(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const CombineTable = createAsyncThunk(
  `${namespace}/fetchCombineTable`,
  async (obj, { rejectWithValue }) => {
    return await restaurantService
      .combineTable(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const UnCombineTable = createAsyncThunk(
  `${namespace}/fetchUnCombineTable`,
  async (obj, { rejectWithValue }) => {
    return await restaurantService
      .unCombineTable(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const setOutStockProduct = createAsyncThunk(
  `${namespace}/setOutStockProduct`,
  async (productId, { rejectWithValue }) => {
    return await productService
      .setOutStockProduct(productId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const restoreOutStockProduct = createAsyncThunk(
  `${namespace}/restoreOutStockProduct`,
  async (productId, { rejectWithValue }) => {
    return await productService
      .restoreOutStockProduct(productId)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const getBillsByDay = createAsyncThunk(
  `${namespace}/getBillsByDay`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .getBillsByDay(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const viewBillDetail = createAsyncThunk(
  `${namespace}/viewBillDetail`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .viewBillDetail(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const getOrderItemsByOrderId = createAsyncThunk(
  `${namespace}/getOrderItemsByOrderId`,
  async (obj, { rejectWithValue }) => {
    return await cashierService
      .getOrderItemsByOrderId(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

export const closeCurrentTable = createAsyncThunk(
  `${namespace}/fetchcloseCurrentTable`,
  async (obj, { rejectWithValue }) => {
    return await restaurantService
      .closeCurrentTable(obj)
      .then((response) => {
        return response.data
      })
      .catch((error) => {
        return rejectWithValue(error)
      })
  }
)

const restaurantSlice = createSlice({
  name: namespace,
  initialState,
  reducers: {
    updateTableStatus: (state, { payload }) => {
      const tableId = payload.table.id
      const tableStatus = payload.table.status
      const tableStatusValue = payload.table.statusValue

      const tableIndex = state.data.tables.findIndex(
        (item) => item.id === tableId
      )

      if (tableIndex >= 0) {
        state.data.tables[tableIndex].status = tableStatus
        state.data.tables[tableIndex].statusValue = tableStatusValue
      }
    },
    updateChangeTableStatus: (state, { payload }) => {
      const tableId = payload.tableId
      const tableStatus = payload.status
      const tableStatusValue = payload.statusValue

      const tableIndex = state.data.tables.findIndex(
        (item) => item.id === tableId
      )

      if (tableIndex >= 0) {
        state.data.tables[tableIndex].status = tableStatus
        state.data.tables[tableIndex].statusValue = tableStatusValue
      }
    },
    updateChangeTableCombineStatus: (state, { payload }) => {
      const tableId = payload.tableCombine.id
      const tableStatus = payload.tableCombine.status
      const tableStatusValue = payload.tableCombine.statusValue

      const tableIndex = state.data.tables.findIndex(
        (item) => item.id === tableId
      )

      if (tableIndex >= 0) {
        state.data.tables[tableIndex].status = tableStatus
        state.data.tables[tableIndex].statusValue = tableStatusValue
      }
    },
    updateCurrentTable: (state, action) => {
      const newTable = action.payload
      const tableIndex = state.data.tables.findIndex(
        (item) => item.id === newTable.id
      )

      if (tableIndex >= 0) {
        state.data.currentTable = newTable
      }
    },
    setCurrentTableAddItemStatus: (state, { payload }) => {
      state.data.currentTable.addItemStatus = payload
    },
    setCurrentTableOrderId: (state, { payload }) => {
      state.data.currentTable.orderId = payload
    },
    emptyCurrentTable: (state) => {
      state.data.currentTable = {}
    },

    resetTableItem: (state, { payload }) => {
      const tableId = payload

      const tableIndex = state.data.tables.findIndex(
        (item) => item.id === tableId
      )

      if (tableIndex >= 0) {
        state.data.tables[tableIndex].status = EnumStatus.EMPTY.status
        state.data.tables[tableIndex].statusValue = EnumStatus.EMPTY.statusValue
      }
    },
    setDataLoading: (state, { payload }) => {
      state.data.loading = payload
    },
    setProductsSearch: (state, action) => {
      state.data.productsSearch = action.payload
    },
    updateBaseData: (state, { payload }) => {
      let newBaseData = payload
      state.data.products = newBaseData.products
      state.data.tables = newBaseData.tables
      state.data.categories = newBaseData.categories
    },
    updateListBillShow: (state, { payload }) => {
      let newListBillShow = payload
      state.data.listBillShow = newListBillShow
    },
    updateCurrentBill: (state, { payload }) => {
      state.data.currentBill = payload
    },
    updateCurrenrOrderitemsBill: (state, { payload }) => {
      state.data.currenrOrderitemsBill = payload
    },
  },
  extraReducers(builder) {
    builder
      .addCase(fetchRestaurantsData.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(fetchRestaurantsData.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        let categories = payload.categories
        categories.unshift({ title: 'Tất cả', id: 0 })
        state.data.categories = categories
        state.data.products = payload.products
        state.data.tables = payload.tables
      })
      .addCase(fetchRestaurantsData.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED

        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })

      .addCase(fetchTablesAndCategoriesData.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(fetchTablesAndCategoriesData.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        let categories = payload.categories
        categories.unshift({ title: 'Tất cả', id: 0 })
        state.data.categories = categories
        state.data.tables = payload.tables
      })
      .addCase(fetchTablesAndCategoriesData.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED

        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })

      .addCase(fetchProductsData.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(fetchProductsData.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        state.data.products = payload.products
      })
      .addCase(fetchProductsData.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED

        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })

      .addCase(fetchChangeTableStatus.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(fetchChangeTableStatus.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
        const newTable = payload
        const tableIndex = state.data.tables.findIndex(
          (item) => item.id === newTable.id
        )
        if (tableIndex >= 0) {
          state.data.tables[tableIndex] = newTable
        }
      })
      .addCase(fetchChangeTableStatus.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })

      .addCase(changeTable.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(changeTable.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
        state.data.tables = payload
      })
      .addCase(changeTable.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })

      .addCase(CombineTable.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(CombineTable.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
        const currentTable = payload.currentTable
        const targetTable = payload.targetTable

        const tableCurrentIndex = state.data.tables.findIndex(
          (item) => item.id === currentTable.id
        )

        if (tableCurrentIndex >= 0) {
          state.data.tables[tableCurrentIndex] = currentTable
        }

        const targetTableIndex = state.data.tables.findIndex(
          (item) => item.id === targetTable.id
        )

        if (targetTableIndex >= 0) {
          state.data.tables[targetTableIndex] = targetTable
        }
      })
      .addCase(CombineTable.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })

      .addCase(UnCombineTable.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(UnCombineTable.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        const currentTable = payload.currentTable
        const targetTable = payload.targetTable

        const tableCurrentIndex = state.data.tables.findIndex(
          (item) => item.id === currentTable.id
        )
        if (tableCurrentIndex >= 0) {
          state.data.tables[tableCurrentIndex] = currentTable
        }
        const targetTableIndex = state.data.tables.findIndex(
          (item) => item.id === targetTable.id
        )
        if (targetTableIndex >= 0) {
          state.data.tables[targetTableIndex] = targetTable
        }
      })
      .addCase(UnCombineTable.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })
      .addCase(setOutStockProduct.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(setOutStockProduct.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
        const newProduct = payload
        const productIndex = state.data.products.findIndex(
          (item) => item.productId === newProduct.productId
        )
        if (productIndex >= 0) {
          state.data.products[productIndex] = newProduct
        }
      })
      .addCase(setOutStockProduct.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })
      .addCase(restoreOutStockProduct.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(restoreOutStockProduct.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED
        const newProduct = payload
        const productIndex = state.data.products.findIndex(
          (item) => item.productId === newProduct.productId
        )
        if (productIndex >= 0) {
          state.data.products[productIndex] = newProduct
        }
      })
      .addCase(restoreOutStockProduct.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        if (payload.response) {
          state.errorMessage = payload.response.statusText
          state.errorStatus = payload.response.status
        }
      })
      .addCase(getBillsByDay.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(getBillsByDay.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        state.data.listBillToday = payload.listBillToday
        state.data.listBillPreviousDay = payload.listBillPreviousDay
        state.data.listBillShow = payload.listBillToday.reverse()
      })
      .addCase(getBillsByDay.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
      })
      .addCase(closeCurrentTable.pending, (state) => {
        state.status = HTTP_STATUS.PENDING
      })
      .addCase(closeCurrentTable.fulfilled, (state, { payload }) => {
        state.status = HTTP_STATUS.FULFILLED

        const currentTable = payload.currentTable

        const tableCurrentIndex = state.data.tables.findIndex(
          (item) => item.id === currentTable.id
        )
        if (tableCurrentIndex >= 0) {
          state.data.tables[tableCurrentIndex] = currentTable
        }
      })
      .addCase(closeCurrentTable.rejected, (state, { payload }) => {
        state.status = HTTP_STATUS.REJECTED
        state.errorMessage = payload.response.statusText
        state.errorStatus = payload.response.status
      })
  },
})

const { reducer, actions } = restaurantSlice
export const {
  updateTableStatus,
  updateChangeTableStatus,
  updateChangeTableCombineStatus,
  updateCurrentTable,
  setCurrentTableAddItemStatus,
  setCurrentTableOrderId,
  setProductsSearch,
  emptyCurrentTable,
  resetTableItem,
  setDataLoading,
  updateBaseData,
  updateListBillShow,
  updateCurrentBill,
  updateCurrenrOrderitemsBill,
} = actions

export default reducer
