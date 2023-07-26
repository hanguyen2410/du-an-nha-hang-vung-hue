import axiosClient from './axiosClient'
import { CASHIER_URL } from 'constants/global'

const restaurantService = {
  getAlls: async () => {
    return await axiosClient.get(CASHIER_URL.RESTAURANT_API_URL)
  },
  getAllTablesAndCategories: async () => {
    return await axiosClient.get(CASHIER_URL.TABLES_AND_CATEGORIES_API_URL)
  },
  getAllProducts: async () => {
    return await axiosClient.get(CASHIER_URL.PRODUCT_API_URL)
  },
  changeStatusTable: async (table) => {
    return axiosClient.post(
      `${CASHIER_URL.TABLE_API_URL}/status/${table.id}`,
      table
    )
  },
  combineTable: async (obj) => {
    return axiosClient.post(`${CASHIER_URL.TABLE_API_URL}/combine-tables`, obj)
  },
  unCombineTable: async (obj) => {
    return axiosClient.post(
      `${CASHIER_URL.TABLE_API_URL}/un-combine-tables`,
      obj
    )
  },
  closeCurrentTable: async (obj) => {
    return axiosClient.post(`${CASHIER_URL.TABLE_API_URL}/close-table`, obj)
  },
}

export default restaurantService
