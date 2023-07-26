const SERVER_API = process.env.REACT_APP_API_URL
export const SOCKET_SERVER = process.env.REACT_APP_SOCKET_SERVER
const COUDINARY_URL = process.env.REACT_APP_COUDINARY_URL

export const LOCATION_URL = 'https://vapi.vnappmob.com/api/province/'
export const AUTH_LOGIN_URL = SERVER_API + '/auth/login'
export const CUSTOMER_URL = SERVER_API + '/customers'
export const ORDER_API = SERVER_API + '/orders'
export const ORDER_ITEM_API = SERVER_API + '/order-items'

export const HTTP_STATUS = Object.freeze({
  IDLE: 'IDLE',
  PENDING: 'PENDING',
  FULFILLED: 'FULFILLED',
  REJECTED: 'REJECTED',
})

export const TABLE_STATUS = Object.freeze({
  NEW: 'NEW',
  UPDATE: 'UPDATE',
  EMPTY: 'EMPTY',
  BUSY: 'BUSY',
})

export const ROLES = Object.freeze({
  ADMIN: 'ADMIN',
  CASHIER: 'CASHIER',
  WAITER: 'WAITER',
  KITCHEN: 'KITCHEN',
})

export const COUDINARY = {
  url: COUDINARY_URL,
  SCALE_IMAGE_300_300: 'c_limit,w_300,h_300,q_100',
  SCALE_IMAGE_180_180: 'c_limit,w_180,h_180,q_100',
  SCALE_IMAGE_120_120: 'c_limit,w_120,h_120,q_100',
}

export const CASHIER_URL = {
  RESTAURANT_API_URL: `${SERVER_API}/cashiers`,

  TABLES_AND_CATEGORIES_API_URL: `${SERVER_API}/cashiers/get-all-tables-and-categories`,

  PRODUCT_API_URL: `${SERVER_API}/cashiers/get-all-products`,

  TABLE_API_URL: `${SERVER_API}/tables`,

  CATEGORY_API_URL: `${SERVER_API}/categories`,

  ORDER_API_URL: `${SERVER_API}/orders/create-with-order-item`,

  ADD_ITEM_KITCHEN: `${ORDER_API}`,

  GET_ALL_ITEMS_BY_TABLE_ID: `${SERVER_API}/orders/get-all-items/`,

  DELETE_ITEM_STOCK_OUT: `${ORDER_ITEM_API}/cashier/delete-item-stock-out`,

  ORDERGET_API_URL: `${ORDER_ITEM_API}/cashier?orderId=`,

  PAY_API_URL: `${SERVER_API}/orders/pay/`,

  PAY_BILL_AND_CHANGE_ORDERiTEM_DONE: `${ORDER_ITEM_API}/kitchen/table/change-status-delivery-to-done-all-products?orderId=`,

  CREATE_BILL: `${SERVER_API}/bills/`,

  PAY_BILL: `${SERVER_API}/bills/pay`,

  CHANGE_TABLE: `${SERVER_API}/tables/change-table`,

  SET_OUT_STOCK_PRODUCT_URL: `${SERVER_API}/products/set-out-stock-product`,

  RESTORE_OUT_STOCK_PRODUCT_URL: `${SERVER_API}/products/restore-out-stock-product`,

  GET_BILL_BY_DAY: `${SERVER_API}/bills/get-bills-today-and-previous-day`,

  VIEW_DETAIL_BILL: `${SERVER_API}/bills/`,

  GET_ORDERITEMS_BY_ORDER_ID: `${SERVER_API}/orders/list-order-items-sum/`,
}

export const KITCHEN_URL = {
  GET_ALL: `${ORDER_ITEM_API}/kitchen/get-all`,

  LIST_ITEMS_API_URL: `${ORDER_ITEM_API}/kitchen/get-by-status-cooking`,

  // LIST_TABLESITEMSCOOKING_API_URL: `${ORDER_ITEM_API}/kitchen/get-by-status-cooking-group-table`,

  LIST_WAITER_API_URL: `${ORDER_ITEM_API}/kitchen/get-by-status-waiter`,

  // COOKINGALLBYPRODUCT_API_URL: `${ORDER_ITEM_API}/kitchen/change-status-cooking-to-waiter-to-product-all?`,

  // COOKINGONEBYPRODUCT_API_URL: `${ORDER_ITEM_API}/kitchen/change-status-cooking-to-waiter-to-product?`,

  // COOKINGALLBYTABLE_API_URL: `${ORDER_ITEM_API}/kitchen/change-status-cooking-to-waiter-to-table-all?`,

  // COOKINGALLBYPRODUCTTABLE_API_URL: `${ORDER_ITEM_API}/kitchen/change-status-cooking-to-waiter-to-product-of-table?`,

  // COOKINGONEBYPRODUCTTABLE_API_URL: `${ORDER_ITEM_API}/kitchen/change-status-cooking-to-waiter-to-table?orderItemId=`,

  // WAITERALL_API_URL: `${ORDER_ITEM_API}/kitchen/change-status-waiter-to-delivery-to-product-of-table?`,

  // WAITERONE_API_URL: `${ORDER_ITEM_API}/kitchen/change-status-waiter-to-delivery-to-table?orderItemId=`,

  CHANGE_COOKING_TO_WAITER_ONE_PRODUCT_OF_PRODUCT_GROUP: `${ORDER_ITEM_API}/kitchen/product/change-status-cooking-to-waiter-one-product`,

  CHANGE_COOKING_TO_WAITER_ALL_PRODUCT_OF_PRODUCT_GROUP: `${ORDER_ITEM_API}/kitchen/product/change-status-cooking-to-waiter-all-products`,

  CHANGE_COOKING_TO_WAITER_ONE_PRODUCT_OF_TABLE_GROUP: `${ORDER_ITEM_API}/kitchen/table/change-status-cooking-to-waiter`,

  CHANGE_COOKING_TO_WAITER_TO_PRODUCT_OF_TABLE_GROUP: `${ORDER_ITEM_API}/kitchen/table/change-status-cooking-to-waiter-to-product`,

  CHANGE_WAITER_TO_STOCK_OUT_ALL_PRODUCT_OF_TABLE: `${ORDER_ITEM_API}/kitchen/table/change-status-cooking-to-waiter-all-products`,

  CHANGE_WAITER_TO_DELIVERY: `${ORDER_ITEM_API}/kitchen/product/change-status-waiter-to-delivery`,

  CHANGE_WAITER_TO_DELIVERY_ALL_PRODUCT_OF_PRODUCT_GROUP: `${ORDER_ITEM_API}/kitchen/table/change-status-waiter-to-delivery-to-product`,

  CHANGE_COOKING_TO_STOCK_OUT_ONE_PRODUCT_OF_TABLE_GROUP: `${ORDER_ITEM_API}/kitchen/table/change-status-cooking-to-stock-out`,

  CHANGE_COOKING_TO_STOCK_OUT_TO_PRODUCT_OF_TABLE_GROUP: `${ORDER_ITEM_API}/kitchen/table/change-status-cooking-to-stock-out-to-product`,

  CHANGE_WAITER_TO_STOCK_OUT: `${ORDER_ITEM_API}/kitchen/product/change-status-waiter-to-stock-out`,

  CHANGE_WAITER_TO_STOCK_OUT_ALL_PRODUCT_OF_PRODUCT_GROUP: `${ORDER_ITEM_API}/kitchen/table/change-status-waiter-to-stock-out-to-product`,

  // RemoveOrderItem_API_URL: `${ORDER_ITEM_API}/delete/`,

  // RemoveOrder_API_URL: `${ORDER_API}/delete/`,
}
