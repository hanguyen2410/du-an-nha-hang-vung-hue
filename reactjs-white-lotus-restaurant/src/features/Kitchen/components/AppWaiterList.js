import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'

// import PropTypes from 'prop-types'

import {
  changeWaiterToDelivery,
  changeWaiterToDeliveryAllProductOfProductGroup,
  changeWaiterToStockOut,
  changeWaiterToStockOutToProductOfOrder,
} from 'features/Kitchen/kitchenSlice'

// @mui
import { Typography, Grid, Stack } from '@mui/material'

// import Paper from '@mui/material/Paper';

import { LoadingButton } from '@mui/lab'
import Box from '@mui/material/Box'

import Iconify from 'components/Iconify'
import { toast } from 'react-toastify'
import { HTTP_STATUS } from './../../../constants/global'

import authService from 'services/authService'

const accessToken = localStorage.getItem('wl_accessToken')

// ----------------------------------------------------------------------

const style = {
  position: 'relative',
  width: '100%',
  paddingTop: '4px',
  bgcolor: 'background.paper',
  borderRadius: '12px',
  marginBottom: '1px',
  border: '1px solid #F9FAFB',
  '&:hover': {
    border: '1px solid #0a9275',
  },
}

AppWaiterList.prototype = {
  //   listWaiter: PropTypes.array,
  // handleWaiterALL: PropTypes.func,
  // loadingAllWaiter: PropTypes.bool,
  // handleWaiterONE: PropTypes.func,
  // loadingOneWaiter: PropTypes.bool,
}

export default function AppWaiterList() {
  const socket = authService.connectServer()

  const dispatch = useDispatch()

  const kitchenData = useSelector((state) => state.kitchenData.data)
  const kitchenDataStatus = useSelector((state) => state.kitchenData.status)
  const orderItemsWaiter = kitchenData.orderItemsWaiter

  const [state, setState] = useState({
    indexLoading: -1,
  })

  // const [state, setState] = useState({
  //     indexAllWaiter: -1,
  //     indexOneWaiter: -1,
  // });

  // const { indexAllWaiter, indexOneWaiter } = state;

  // const handleClickAllWaiter = (orderItemId, index, orderId, tableId) => {
  //     setState({
  //         ...state,
  //         indexAllWaiter: index,
  //     });
  //     handleWaiterALL(orderItemId, index, orderId, tableId);
  // };

  const handleWaiterToStockOut = (
    tableId,
    tableName,
    orderItemId,
    productTitle,
    note,
    index
  ) => {
    setState({
      ...state,
      indexLoading: orderItemId + '_' + index,
    })

    let obj = {
      orderItemId: orderItemId,
      note: note,
    }

    const changeWaiterToStockOutAction = changeWaiterToStockOut(obj)
    dispatch(changeWaiterToStockOutAction)
      .unwrap()
      .then((data) => {
        const tableObj = {
          tableId,
          tableName,
          orderItemId: orderItemId,
          note: note,
        }

        socket.emit(
          'change-waiter-to-stockout-success-of-kitchen',
          accessToken,
          tableObj
        )

        toast.warning(`Thêm 1 '${productTitle}' của ${tableName} đã hết hàng`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn '${productTitle}' thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoading: -1,
        })
      })
  }

  const handleWaiterToStockOutToProductOfOrder = (
    tableId,
    tableName,
    orderItemId,
    productTitle,
    note,
    index
  ) => {
    setState({
      ...state,
      indexLoading: orderItemId + '_' + index,
    })

    let obj = {
      orderItemId: orderItemId,
      note: note,
    }

    const changeWaiterToStockOutToProductOfOrderAction =
      changeWaiterToStockOutToProductOfOrder(obj)
    dispatch(changeWaiterToStockOutToProductOfOrderAction)
      .unwrap()
      .then(() => {
        // const tableObj = {
        //   tableId,
        //   tableName,
        // }
        // socket.emit('change-cooking-to-stock-out-success', tableObj)

        const tableObj = {
          tableId,
          tableName,
          orderItemId: orderItemId,
          note: note,
        }

        socket.emit(
          'change-waiter-to-stockout-success-of-kitchen',
          accessToken,
          tableObj
        )

        toast.warning(`'${productTitle}' của ${tableName} đã hết hàng`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn '${productTitle}' thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoading: -1,
        })
      })
  }

  const handleClickOneWaiter = (
    tableId,
    tableName,
    orderItemId,
    productTitle,
    note,
    index
  ) => {
    setState({
      ...state,
      indexLoading: orderItemId + '_' + index,
    })

    let obj = {
      orderItemId: orderItemId,
      note: note,
    }

    const changeWaiterToDeliveryAction = changeWaiterToDelivery(obj)
    dispatch(changeWaiterToDeliveryAction)
      .unwrap()
      .then(() => {
        const tableObj = {
          tableId,
          tableName,
        }
        socket.emit('change-waiter-to-delivery-success', tableObj)

        socket.emit('change-waiter-to-delivery-success-of-kitchen', accessToken)

        toast.success(`Thêm 1 '${productTitle}' của ${tableName} đã giao xong`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn '${productTitle}' thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoading: -1,
        })
      })
  }

  const handleClickAllWaiterToDelivery = (
    orderItemId,
    productTitle,
    note,
    tableId,
    tableName,
    index
  ) => {
    setState({
      ...state,
      indexLoading: orderItemId + '_' + index,
    })

    const obj = {
      orderItemId,
      note,
    }

    dispatch(changeWaiterToDeliveryAllProductOfProductGroup(obj))
      .unwrap()
      .then((data) => {
        const tableObj = {
          tableId,
          tableName,
        }
        socket.emit('change-waiter-to-delivery-success', tableObj)

        socket.emit('change-waiter-to-delivery-success-of-kitchen', accessToken)

        toast.success(`'${productTitle}' của ${tableName} đã giao xong`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn '${productTitle}' thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoading: -1,
        })
      })
  }
  const { indexLoading } = state
  return (
    <>
      {orderItemsWaiter.length > 0 &&
        orderItemsWaiter.map((orderItem, index) => (
          <Box sx={style} key={orderItem.orderItem + '_' + index}>
            <Grid>
              <Stack
                direction="row"
                alignItems="center"
                justifyContent="space-between"
                sx={{
                  padding: '12px 5px',
                  // border: '1px solid white',
                  // borderRadius: '12px',
                }}
              >
                <Stack direction="column" alignItems="flex-start">
                  <Stack direction="row" alignItems="center">
                    <Typography
                      id="transition-modal-title"
                      variant="body2"
                      sx={{
                        color: '#0a9275',
                        lineHeight: 0.6,
                        textTransform: 'uppercase',
                        fontSize: '16px',
                        fontWeight: 'bold',
                        fontFamily: 'serif',
                        mt: 1,
                      }}
                    >
                      {orderItem.productTitle}
                    </Typography>
                    <Typography
                      variant="body2"
                      sx={{
                        color: '#d52323',
                        fontFamily: 'sans-serif',
                        ml: 1,
                      }}
                    >
                      {orderItem.unitTitle}
                    </Typography>
                  </Stack>

                  <Stack direction="row" alignItems="center">
                    <Typography
                      id="transition-modal-title"
                      variant="body2"
                      sx={{
                        color: '#a30101',
                        lineHeight: 0.6,
                        fontSize: '16px',
                        fontWeight: 'bold',
                        fontFamily: 'serif',
                        mt: 2,
                      }}
                    >
                      {orderItem.note}
                    </Typography>
                  </Stack>
                </Stack>

                <Stack direction="row" alignItems="center">
                  {!orderItem.cooking ? (
                    <>
                      <LoadingButton
                        variant="contained"
                        sx={{
                          minWidth: '48px',
                          borderRadius: '20px',
                          backgroundColor: 'red',
                          color: 'white',
                          boxShadow: 'none',
                          border: '1px solid red',
                          '&:hover': {
                            backgroundColor: '#fff',
                            color: 'red',
                          },
                          mr: 1,
                          transform: 'rotate(180deg)',
                        }}
                        onClick={() => {
                          handleWaiterToStockOutToProductOfOrder(
                            orderItem.tableId,
                            orderItem.tableName,
                            orderItem.orderItemId,
                            orderItem.productTitle,
                            orderItem.note,
                            index
                          )
                        }}
                      >
                        <Iconify icon={'ic:outline-double-arrow'} width={20} />
                      </LoadingButton>
                      <LoadingButton
                        variant="contained"
                        sx={{
                          minWidth: '48px',
                          borderRadius: '20px',
                          backgroundColor: '#fff',
                          color: 'red',
                          boxShadow: 'none',
                          border: '1px solid red',
                          mr: 2.5,
                          '&:hover': {
                            backgroundColor: 'red',
                            color: 'white',
                          },
                        }}
                        onClick={() => {
                          handleWaiterToStockOut(
                            orderItem.tableId,
                            orderItem.tableName,
                            orderItem.orderItemId,
                            orderItem.productTitle,
                            orderItem.note,
                            index
                          )
                        }}
                      >
                        <Iconify
                          icon={'ic:sharp-keyboard-arrow-left'}
                          width={20}
                        />
                      </LoadingButton>
                    </>
                  ) : (
                    <></>
                  )}

                  <Typography
                    id="transition-modal-title"
                    variant="body2"
                    sx={{
                      color: 'black',
                      fontSize: '24px',
                      fontWeight: 'bold',
                      fontFamily: 'serif',
                      mr: 4,
                    }}
                  >
                    {orderItem.quantity}
                  </Typography>
                  <Stack
                    direction="column"
                    alignItems="flex-start"
                    sx={{
                      mr: 3,
                    }}
                  >
                    <Typography
                      id="transition-modal-title"
                      variant="body2"
                      sx={{
                        color: 'black',
                        fontSize: '18px',
                        fontWeight: 'bold',
                        fontFamily: 'serif',
                      }}
                    >
                      {orderItem.tableName}
                    </Typography>

                    <Typography
                      variant="body2"
                      sx={{
                        color: 'gray',
                        fontSize: '12px',
                        fontFamily: 'sans-serif',
                      }}
                    >
                      {/* {Helper.countTime(new Date(), new Date(item.updatedAt))} */}
                    </Typography>
                  </Stack>
                  <LoadingButton
                    loading={
                      kitchenDataStatus === HTTP_STATUS.PENDING &&
                      orderItem.orderItemId + '_' + index === indexLoading
                    }
                    // loading={loadingOneWaiter && index === indexOneWaiter}
                    variant="contained"
                    sx={{
                      minWidth: '48px',
                      borderRadius: '20px',
                      backgroundColor: '#F9FAFB',
                      color: '#28B44F',
                      boxShadow: 'none',
                      border: '1px solid #28B44F',
                      mr: 1,
                      '&:hover': {
                        backgroundColor: '#28B44F',
                        color: 'white',
                      },
                    }}
                    onClick={() => {
                      handleClickOneWaiter(
                        orderItem.tableId,
                        orderItem.tableName,
                        orderItem.orderItemId,
                        orderItem.productTitle,
                        orderItem.note,
                        index
                      )
                    }}
                  >
                    <Iconify
                      icon={'ic:sharp-keyboard-arrow-right'}
                      width={20}
                    />
                  </LoadingButton>
                  <LoadingButton
                    loading={
                      kitchenDataStatus === HTTP_STATUS.PENDING &&
                      orderItem.orderItemId + '_' + index === indexLoading
                    }
                    // loading={loadingAllWaiter && index === indexAllWaiter}
                    variant="contained"
                    sx={{
                      minWidth: '48px',
                      borderRadius: '20px',
                      backgroundColor: '#28B44F',
                      color: 'white',
                      boxShadow: 'none',
                      border: '1px solid #28B44F',
                      '&:hover': {
                        backgroundColor: '#F9FAFB',
                        color: '#28B44F',
                      },
                    }}
                    onClick={() => {
                      handleClickAllWaiterToDelivery(
                        orderItem.orderItemId,
                        orderItem.productTitle,
                        orderItem.note,
                        orderItem.tableId,
                        orderItem.tableName,
                        index
                      )
                    }}
                  >
                    <Iconify icon={'ic:outline-double-arrow'} width={20} />
                  </LoadingButton>
                </Stack>
              </Stack>
            </Grid>
          </Box>
        ))}
    </>
  )
}
