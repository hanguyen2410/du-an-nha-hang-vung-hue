import axiosClient from './axiosClient'
import { CASHIER_URL } from 'constants/global'

const productService = {
  getAlls: async () => {
    return await axiosClient.get(CASHIER_URL.PRODUCT_API_URL)
  },
  setOutStockProduct: async (productId) => {
    return await axiosClient.post(`${CASHIER_URL.SET_OUT_STOCK_PRODUCT_URL}/${productId}`)
  },
  restoreOutStockProduct: async (productId) => {
    return await axiosClient.post(`${CASHIER_URL.RESTORE_OUT_STOCK_PRODUCT_URL}/${productId}`)
  },
}

export default productService
