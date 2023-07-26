// @mui
import { Typography, Grid, Stack } from '@mui/material'
import { styled } from '@mui/material/styles'

// SweetAlert
import Swal from 'sweetalert2'
import 'layouts/sweetalert.css'

// Toast
import { toast } from 'react-toastify'

import Box from '@mui/material/Box'

import Iconify from 'components/Iconify'
import { OrderItemStatus } from 'constants/OrderItemStatus'

import OrderItemAvatar from './OrderItemAvatar'
import OrderItemInfoNEW from './OrderItemInfoNEW'
import OrderItemInfoOTHER from './OrderItemInfoOTHER'
import DeleteOrderItem from './DeleteOrderItem'
import { useDispatch, useSelector } from 'react-redux'
import {
  setTotalAmount,
  deleteUpdateOrderItem,
  deleteItemStockOut,
} from 'features/Home/orderItemSlice'

import {
  increaseQuantityOrderItem,
  decreaseQuantityOrderItem,
} from 'features/Home/orderItemSlice'
import DeleteOrderItemStockOut from './DeleteOrderItemStockOut'

// ----------------------------------------------------------------------

const style = {
  position: 'relative',
  width: '100%',

  paddingTop: '4px',
  bgcolor: 'background.paper',
  // borderRadius: '12px',
  marginBottom: '1px',
  border: '1px solid white',
  '&:hover': {
    border: '1px solid blue',
  },
}
const styleReadonly = {
  position: 'relative',
  width: '100%',
  paddingTop: '4px',
  bgcolor: 'background.paper',
  // borderRadius: '12px',
  marginBottom: '1px',
  border: '1px solid white',
}
const StyledProductImg = styled('img')({
  top: 0,
  width: '100%',
  height: '100%',
  objectFit: 'cover',
  position: 'absolute',
  borderRadius: '12px',
})

export default function AppOrderDetail({
  handleOpenEditModal,
  handleCloseBill,
  closeTable,
  ...other
}) {
  const dispatch = useDispatch()

  const baseData = useSelector((state) => state.baseData.data)
  const currentTable = baseData.currentTable

  const orderItemData = useSelector((state) => state.orderItemData.data)

  const currentOrderItemsdata = orderItemData.currentOrderItemsdata
  const updateOrderItemsdata = orderItemData.updateOrderItemsdata

  let orderItemsData = currentOrderItemsdata.concat(updateOrderItemsdata)

  const handleDecreaseQuantity = (index, orderItem) => {
    dispatch(decreaseQuantityOrderItem(orderItem))
    dispatch(setTotalAmount())
  }
  const handleIncreaseQuantity = (index, orderItem) => {
    dispatch(increaseQuantityOrderItem(orderItem))
    dispatch(setTotalAmount())
  }
  const handleChangeQuantity = (e, index, orderItem) => {
    if (e.target.value >= 1 && e.target.value <= 100) {
      orderItem.quantity = Number(e.target.value)
    }
  }

  const handleClickDeleteItem = (index, orderItem) => {
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        title: 'titleDeleteItem',
      },
    })

    swalWithBootstrapButtons
      .fire({
        title: `Chắc chắn muốn xóa "${orderItem.productTitle}"`,
        showCancelButton: true,
        confirmButtonText: 'Chấp nhận',
        cancelButtonText: `Hủy bỏ`,
      })
      .then((result) => {
        if (result.isConfirmed) {
          handleDeleteOrderItem(index)
          toast.success(`${orderItem.productTitle} xóa thành công`)
        }
      })
  }

  const handleDeleteOrderItem = (indexInOrderItems) => {
    dispatch(deleteUpdateOrderItem(indexInOrderItems))

    dispatch(setTotalAmount())
  }

  const handleClickDeleteItemStockOut = (index, orderItem) => {
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        title: 'titleDeleteItem',
      },
    })

    swalWithBootstrapButtons
      .fire({
        title: `Chắc chắn muốn xóa "${orderItem.productTitle}"`,
        showCancelButton: true,
        confirmButtonText: 'Chấp nhận',
        cancelButtonText: `Hủy bỏ`,
      })
      .then((result) => {
        if (result.isConfirmed) {
          const obj = {
            orderItemId: orderItem.id,
          }

          dispatch(deleteItemStockOut(obj))
            .unwrap()
            .then(() => {
              dispatch(setTotalAmount())

              // const obj = {
              //   tableId: currentTable.id,
              //   status: EnumStatus.EMPTY.status,
              //   statusValue: EnumStatus.EMPTY.statusValue,
              // }

              // const updateTableStatusAction = updateTableStatus(obj)
              // dispatch(updateTableStatusAction)

              // handleCloseBill()
              // closeTable()
            })

          toast.success(`${orderItem.productTitle} xóa thành công`)
        }
      })
  }

  return (
    <>
      {orderItemsData.length > 0 &&
        orderItemsData.map((item, index) =>
          item.status === OrderItemStatus.NEW.status ? (
            <Box sx={style} key={index}>
              <DeleteOrderItem
                item={item}
                index={index}
                handleClickDeleteItem={handleClickDeleteItem}
              />

              <Grid container spacing={1}>
                <OrderItemAvatar
                  orderItem={item}
                  index={index}
                  handleOpenEditModal={handleOpenEditModal}
                />

                <Grid
                  item
                  xs={10}
                  md={10}
                  lg={10}
                  sx={{
                    textAlign: 'justify',
                    top: 0,
                  }}
                >
                  <OrderItemInfoNEW
                    item={item}
                    index={index}
                    handleChangeQuantity={handleChangeQuantity}
                    handleDecreaseQuantity={handleDecreaseQuantity}
                    handleIncreaseQuantity={handleIncreaseQuantity}
                  />

                  <Stack direction="row" alignItems="center">
                    <Iconify
                      icon="fluent-mdl2:status-circle-checkmark"
                      sx={{ color: 'gray' }}
                    />
                    <Typography
                      variant="body2"
                      sx={{
                        color: '#ff4842',
                        fontSize: '12px',
                        fontFamily: 'sans-serif',
                      }}
                    >
                      {OrderItemStatus.NEW.statusValue}
                    </Typography>
                  </Stack>
                </Grid>
              </Grid>
            </Box>
          ) : (
            <Box sx={styleReadonly} key={index}>
              {item.status === OrderItemStatus.STOCK_OUT.status ? (
                <DeleteOrderItemStockOut
                  item={item}
                  index={index}
                  handleClickDeleteItemStockOut={handleClickDeleteItemStockOut}
                />
              ) : (
                <></>
              )}

              <Grid container spacing={1}>
                <OrderItemAvatar
                  orderItem={item}
                  index={index}
                  handleOpenEditModal={handleOpenEditModal}
                />

                <Grid
                  item
                  xs={10}
                  md={10}
                  lg={10}
                  sx={{ textAlign: 'justify', top: 0 }}
                >
                  <OrderItemInfoOTHER item={item} index={index} />

                  <Stack direction="row" alignItems="center">
                    <Iconify
                      icon="fluent-mdl2:status-circle-checkmark"
                      sx={{ color: 'gray' }}
                    />
                    {item.status === OrderItemStatus.STOCK_OUT.status ? (
                      <Typography
                        variant="body2"
                        sx={{
                          color: 'red',
                          fontSize: '12px',
                          fontFamily: 'sans-serif',
                        }}
                      >
                        {OrderItemStatus.STOCK_OUT.statusValue}
                      </Typography>
                    ) : (
                      <Typography
                        variant="body2"
                        sx={{
                          color: 'green',
                          fontSize: '12px',
                          fontFamily: 'sans-serif',
                        }}
                      >
                        {item.status === OrderItemStatus.COOKING.status
                          ? OrderItemStatus.COOKING.statusValue
                          : item.status === OrderItemStatus.WAITER.status
                          ? OrderItemStatus.WAITER.statusValue
                          : item.status === OrderItemStatus.DELIVERY.status
                          ? OrderItemStatus.DELIVERY.statusValue
                          : OrderItemStatus.DONE.statusValue}
                      </Typography>
                    )}
                  </Stack>
                </Grid>
              </Grid>
            </Box>
          )
        )}
    </>
  )
}
