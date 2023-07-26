export const OrderItemStatus = Object.freeze({
  NEW: { status: 'NEW', statusValue: 'Mới' },
  UPDATE: { status: 'UPDATE', statusValue: 'Cập nhật' },
  COOKING: { status: 'COOKING', statusValue: 'Đang làm' },
  WAITER: { status: 'WAITER', statusValue: 'Chờ cung ứng' },
  DELIVERY: { status: 'DELIVERY', statusValue: 'Đã giao' },
  STOCK_OUT: { status: 'STOCK_OUT', statusValue: 'Hết hàng' },
  DONE: { status: 'DONE', statusValue: 'Hoàn tất' },
})
