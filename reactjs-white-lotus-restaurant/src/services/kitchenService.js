import axiosClient from './axiosClient'
import { KITCHEN_URL } from 'constants/global'

const kitchenService = {
  getAllData: async () => {
    return await axiosClient.get(KITCHEN_URL.GET_ALL)
  },
  getListItemsCooking: async () => {
    return await axiosClient.get(KITCHEN_URL.LIST_ITEMS_API_URL)
  },
  getListItemsWaiter: async () => {
    return await axiosClient.get(KITCHEN_URL.LIST_WAITER_API_URL)
  },
  changeCookingToWaiterOneProductOfProductGroup: async (obj) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_COOKING_TO_WAITER_ONE_PRODUCT_OF_PRODUCT_GROUP,
      obj
    )
  },
  changeCookingToWaiterAllProductOfProductGroup: async (obj) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_COOKING_TO_WAITER_ALL_PRODUCT_OF_PRODUCT_GROUP,
      obj
    )
  },
  changeCookingToWaiterOneProductOfTableGroup: async (orderItemId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_COOKING_TO_WAITER_ONE_PRODUCT_OF_TABLE_GROUP +
        '?orderItemId=' +
        orderItemId
    )
  },
  changeCookingToWaiterToProductOfTableGroup: async (orderItemId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_COOKING_TO_WAITER_TO_PRODUCT_OF_TABLE_GROUP +
        '?orderItemId=' +
        orderItemId
    )
  },
  changeStatusFromCookingToWaiterAllProductOfTable: async (orderId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_WAITER_TO_STOCK_OUT_ALL_PRODUCT_OF_TABLE +
        '?orderId=' +
        orderId
    )
  },
  changeWaiterToDelivery: async (orderItemId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_WAITER_TO_DELIVERY + '?orderItemId=' + orderItemId
    )
  },
  changeWaiterToDeliveryAllProductOfProductGroup: async (orderItemId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_WAITER_TO_DELIVERY_ALL_PRODUCT_OF_PRODUCT_GROUP +
        '?orderItemId=' +
        orderItemId
    )
  },
  changeCookingToStockOutOneProductOfTableGroup: async (orderItemId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_COOKING_TO_STOCK_OUT_ONE_PRODUCT_OF_TABLE_GROUP +
        '?orderItemId=' +
        orderItemId
    )
  },
  changeCookingToStockOutToProductOfTableGroup: async (orderItemId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_COOKING_TO_STOCK_OUT_TO_PRODUCT_OF_TABLE_GROUP +
        '?orderItemId=' +
        orderItemId
    )
  },
  changeWaiterToStockOut: async (orderItemId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_WAITER_TO_STOCK_OUT + '?orderItemId=' + orderItemId
    )
  },
  changeWaiterToStockOutToProductOfOrder: async (orderItemId) => {
    return await axiosClient.post(
      KITCHEN_URL.CHANGE_WAITER_TO_STOCK_OUT_ALL_PRODUCT_OF_PRODUCT_GROUP +
        '?orderItemId=' +
        orderItemId
    )
  },
}

export default kitchenService
