import axiosClient from './axiosClient'
import { CASHIER_URL } from 'constants/global'

const cashierService = {
  getTables: async () => {
    return await axiosClient.get(CASHIER_URL.TABLE_API_URL)
  },
  getOrderByTable: async (tableId) => {
    return await axiosClient.get(
      CASHIER_URL.GET_ALL_ITEMS_BY_TABLE_ID + tableId
    )
  },
  changeStatusTable: async (id, table) => {
    return axiosClient.post(`${CASHIER_URL.TABLE_API_URL}/status/${id}`, table)
  },
  getProducts: async () => {
    return axiosClient.get(CASHIER_URL.PRODUCT_API_URL)
  },
  getCategories: async () => {
    return axiosClient.get(CASHIER_URL.CATEGORY_API_URL)
  },
  getOrderItemsByTableId: async (tableId) => {
    return await axiosClient.get(
      CASHIER_URL.GET_ALL_ITEMS_BY_TABLE_ID + tableId
    )
  },
  addItemKitchen: async (obj) => {
    return axiosClient.post(`${CASHIER_URL.ADD_ITEM_KITCHEN}`, obj)
  },
  deleteItemStockOut: async (orderItemId) => {
    return axiosClient.delete(
      `${CASHIER_URL.DELETE_ITEM_STOCK_OUT}?orderItemId=${orderItemId}`
    )
  },
  payBillAndChangeOrderItemsDONE: async (idOrder) => {
    return axiosClient.post(
      `${CASHIER_URL.PAY_BILL_AND_CHANGE_ORDERiTEM_DONE}` + `${idOrder}`
    )
  },
  createBill: async (obj) => {
    return axiosClient.post(`${CASHIER_URL.CREATE_BILL}`, obj)
  },
  payBill: async (obj) => {
    return axiosClient.post(`${CASHIER_URL.PAY_BILL}`, obj)
  },
  changeTable: async (obj) => {
    return axiosClient.post(`${CASHIER_URL.CHANGE_TABLE}`, obj)
  },
  getBillsByDay: async (obj) => {
    return axiosClient.get(`${CASHIER_URL.GET_BILL_BY_DAY}`)
  },
  viewBillDetail: async (billId) => {
    return axiosClient.get(`${CASHIER_URL.VIEW_DETAIL_BILL}` + `${billId}`)
  },
  getOrderItemsByOrderId: async (orderId) => {
    return axiosClient.get(
      `${CASHIER_URL.GET_ORDERITEMS_BY_ORDER_ID}` + `${orderId}`
    )
  },
}

export default cashierService
