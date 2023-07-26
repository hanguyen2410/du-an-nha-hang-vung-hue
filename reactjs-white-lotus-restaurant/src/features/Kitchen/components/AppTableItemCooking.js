import { useState } from 'react'
import { useDispatch } from 'react-redux'

// import PropTypes from 'prop-types'

// @mui
import { Typography, Grid, Stack } from '@mui/material'

// import Paper from '@mui/material/Paper';

import { LoadingButton } from '@mui/lab'
import Box from '@mui/material/Box'

import {
  changeCookingToStockOutOneProductOfTableGroup,
  changeCookingToStockOutToProductOfTableGroup,
  changeCookingToWaiterOneProductOfTableGroup,
  changeCookingToWaiterToProductOfTableGroup,
  changeStatusFromCookingToWaiterAllProductOfTable,
} from 'features/Kitchen/kitchenSlice'

import Iconify from 'components/Iconify'
import Helper from 'utils/Helper'
import { toast } from 'react-toastify'

import authService from 'services/authService'

const accessToken = localStorage.getItem('wl_accessToken')

const style = {
  position: 'relative',
  width: '100%',
  paddingTop: '4px',
  // padding: '20px',
  bgcolor: 'background.paper',
  // borderRadius: '12px',
  // marginBottom: '1px',
  border: '1px solid #F9FAFB',
  '&:hover': {
    border: '1px solid #7266BA',
  },
}

AppTableItemCooking.prototype = {
  //   listTableItemsCooking: PropTypes.array,
  // handleCookingAllByTable: PropTypes.func,
  // handleCookingAllByProductTable: PropTypes.func,
  // loadingAllTable: PropTypes.bool,
  // loadingAllProductTable: PropTypes.bool,
  // loadingOneProductTable: PropTypes.bool,
  // handleCookingOneByTable: PropTypes.func,
  // handleRemoveItem: PropTypes.func,
  // handleRemoveOrder: PropTypes.func,
}

export default function AppTableItemCooking({ orderItemsTable }) {
  const socket = authService.connectServer()

  const dispatch = useDispatch()

  // const kitchenDataStatus = useSelector((state) => state.kitchenData.status)

  const [state, setState] = useState({
    // loadingOneProductItem: false,
    indexLoading: -1,
    indexLoadingAllTable: -1,
    indexLoadingOneProduct: -1,
    indexLoadingAllProduct: -1,
    checkShowTableDetail: [true, true, true, true],
  })

  const [itemIndexLoading, setItemIndexLoading] = useState([])

  const handleClickTableDetail = (index) => {
    const list = [...checkShowTableDetail]
    list[index] = !list[index]

    setState({
      ...state,
      checkShowTableDetail: list,
    })
  }

  const handleCookingToStockOutOneProduct = (
    tableId,
    tableName,
    orderItemId,
    productId,
    productTitle,
    indexItem
  ) => {
    setState({
      ...state,
      indexLoading: orderItemId + '_' + indexItem,
    })

    setItemIndexLoading((current) => [
      ...current,
      tableId + '_' + productId + '_' + orderItemId,
    ])

    let obj = {
      orderItemId,
    }

    const action = changeCookingToStockOutOneProductOfTableGroup(obj)
    dispatch(action)
      .unwrap()
      .then((data) => {
        const tableObj = {
          tableId,
          tableName,
        }

        socket.emit(
          'change-cooking-to-stock-out-success',
          tableObj,
          accessToken
        )

        // socket.emit(
        //   'change-cooking-to-stock-out-success-of-kitchen',
        //   accessToken
        // )

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

        setItemIndexLoading(
          itemIndexLoading.filter(
            (item) => item !== tableId + '_' + productId + '_' + orderItemId
          )
        )
      })
  }

  const handleCookingToStockOutToProduct = (
    tableId,
    tableName,
    orderItemId,
    productId,
    productTitle,
    indexItem
  ) => {
    setState({
      ...state,
      indexLoading: orderItemId + '_' + indexItem,
    })

    setItemIndexLoading((current) => [
      ...current,
      tableId + '_' + productId + '_' + orderItemId,
    ])

    let obj = {
      orderItemId,
    }

    const action = changeCookingToStockOutToProductOfTableGroup(obj)
    dispatch(action)
      .unwrap()
      .then((data) => {
        const tableObj = {
          tableId,
          tableName,
        }

        socket.emit(
          'change-cooking-to-stock-out-success',
          tableObj,
          accessToken
        )

        // socket.emit('change-cooking-to-stock-out-success-of-kitchen', accessToken)

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

        setItemIndexLoading(
          itemIndexLoading.filter(
            (item) => item !== tableId + '_' + productId + '_' + orderItemId
          )
        )
      })
  }

  const changeCookingToWaiterOneProduct = (
    tableId,
    tableName,
    orderItemId,
    productId,
    productTitle,
    indexItem
  ) => {
    setState({
      ...state,
      indexLoading: productId + '_' + indexItem,
    })

    setItemIndexLoading((current) => [
      ...current,
      tableId + '_' + productId + '_' + orderItemId,
    ])

    const obj = {
      orderItemId,
    }

    const action = changeCookingToWaiterOneProductOfTableGroup(obj)
    dispatch(action)
      .unwrap()
      .then((data) => {
        const tableObj = {
          tableId,
          tableName,
        }

        socket.emit('change-cooking-to-waiter-success', tableObj, accessToken)

        // socket.emit('change-cooking-to-waiter-success-of-kitchen', accessToken)

        toast.success(`Thêm 1 '${productTitle}' của ${tableName} đã làm xong`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoading: -1,
        })

        setItemIndexLoading(
          itemIndexLoading.filter(
            (item) => item !== tableId + '_' + productId + '_' + orderItemId
          )
        )
      })
  }

  const handleCookingToWaiterAllProductOfProductTable = (
    tableId,
    tableName,
    orderItemId,
    productId,
    productTitle,
    indexItem
  ) => {
    setState({
      ...state,
      indexLoading: productId + '_' + indexItem,
    })

    setItemIndexLoading((current) => [
      ...current,
      tableId + '_' + productId + '_' + orderItemId,
    ])

    const obj = {
      orderItemId,
    }

    const action = changeCookingToWaiterToProductOfTableGroup(obj)
    dispatch(action)
      .unwrap()
      .then((data) => {
        const tableObj = {
          tableId,
          tableName,
        }

        socket.emit('change-cooking-to-waiter-success', tableObj, accessToken)

        // socket.emit('change-cooking-to-waiter-success-of-kitchen', accessToken)

        toast.success(`'${productTitle}' của ${tableName} đã làm xong`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoading: -1,
        })

        setItemIndexLoading(
          itemIndexLoading.filter(
            (item) => item !== tableId + '_' + productId + '_' + orderItemId
          )
        )
      })
  }

  const handleClickAllByTable = (orderId, indexTable, tableId, tableName) => {
    setState({
      ...state,
      indexLoadingAllTable: indexTable,
    })

    let obj = {
      orderId: orderId,
      indexTable: indexTable,
      tableId: tableId,
      tableName: tableName,
    }

    dispatch(changeStatusFromCookingToWaiterAllProductOfTable(obj))
      .unwrap()
      .then(() => {
        const tableObj = {
          tableId,
          tableName,
        }

        socket.emit(
          'change-cooking-to-waiter-success-all-product-of-table',
          tableObj,
          accessToken
        )

        // socket.emit('change-cooking-to-waiter-success-of-kitchen', accessToken)

        toast.success(`Tất cả sản phẩm của ${obj.tableName} đã làm xong`)
      })
      .catch(() => {
        toast.error(`Chuyển trạng thái món ăn thất bại`)
      })
      .finally(() => {
        setState({
          ...state,
          indexLoadingAllTable: -1,
        })
      })
  }

  const {
    // indexLoading,
    indexLoadingAllTable,
    // indexLoadingOneProduct,
    // indexLoadingAllProduct,
    checkShowTableDetail,
  } = state

  return (
    <>
      {orderItemsTable.map((item, indexTable) => (
        <Stack key={item.tableId}>
          <Box sx={style}>
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
                    {checkShowTableDetail[indexTable] === true ? (
                      <Iconify
                        icon={'ic:baseline-plus'}
                        bgcolor="white"
                        color="#5A5A72"
                        width={25}
                        padding={0.4}
                        cursor="pointer"
                        sx={{
                          borderRadius: '12px',
                          '&:hover': {
                            color: 'white',
                            bgcolor: '#f4557e',
                          },
                        }}
                        onClick={() => {
                          handleClickTableDetail(indexTable)
                        }}
                      />
                    ) : (
                      <Iconify
                        icon={'ic:baseline-minus'}
                        bgcolor="white"
                        color="#5A5A72"
                        width={25}
                        padding={0.4}
                        cursor="pointer"
                        sx={{
                          borderRadius: '12px',
                          '&:hover': {
                            color: 'white',
                            bgcolor: '#f4557e',
                          },
                        }}
                        onClick={() => {
                          handleClickTableDetail(indexTable)
                        }}
                      />
                    )}

                    <Typography
                      id="transition-modal-title"
                      variant="body2"
                      sx={{
                        // color: 'dark',
                        color: '#0066CC',
                        textTransform: 'uppercase',
                        fontSize: '16px',
                        fontWeight: 'bold',
                        fontFamily: 'serif',
                      }}
                    >
                      {item.tableName}
                    </Typography>
                  </Stack>
                </Stack>
                <Stack direction="row" alignItems="center">
                  {/* <Iconify
                    icon={'material-symbols:close'}
                    bgcolor="white"
                    color="#5A5A72"
                    width={25}
                    padding={0.4}
                    cursor="pointer"
                    sx={{
                      mr: 7.5,
                      borderRadius: '12px',
                      '&:hover': {
                        color: 'white',
                        bgcolor: '#7266BA',
                      },
                    }}
                    onClick={() => {
                        handleClickRemoveOrder(table);
                    }}
                  /> */}
                  <Typography
                    id="transition-modal-title"
                    variant="body2"
                    sx={{
                      color: '#1fb43c',
                      fontSize: '22px',
                      fontWeight: 'bold',
                      fontFamily: 'serif',
                      mr: 7.5,
                    }}
                  >
                    {item.countProduct}
                  </Typography>

                  <LoadingButton
                    loading={indexTable === indexLoadingAllTable}
                    variant="contained"
                    sx={{
                      borderRadius: '20px',
                      backgroundColor: '#7266BA',
                      color: 'white',
                      boxShadow: 'none',
                      border: '1px solid #7266BA',
                      '&:hover': {
                        backgroundColor: '#F9FAFB',
                        color: '#7266BA',
                      },
                    }}
                    onClick={() => {
                      handleClickAllByTable(
                        item.orderId,
                        indexTable,
                        item.id,
                        item.tableName
                      )
                    }}
                  >
                    <Iconify icon={'ic:outline-double-arrow'} width={16} />
                  </LoadingButton>
                </Stack>
              </Stack>
            </Grid>
          </Box>
          {item.orderItems.map((orderItem, indexItem) => (
            <Box sx={style} key={orderItem.orderItemId}>
              <Grid>
                <Stack
                  direction="row"
                  alignItems="center"
                  justifyContent="space-between"
                  sx={{
                    padding: '0 5px 10px 60px',
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
                          color: '#5B509A',
                          fontSize: '18px',
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
                      <Typography
                        variant="body2"
                        sx={{
                          color: 'gray',
                          fontSize: '12px',
                          fontFamily: 'sans-serif',
                          ml: 1,
                        }}
                      >
                        {/* {orderItem.updatedAt} */}
                        {Helper.countTime(
                          new Date(),
                          new Date(orderItem.updatedAt)
                        )}
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
                    {/* <Typography
                      variant="body2"
                      sx={{
                        color: 'gray',
                        fontSize: '12px',
                        fontFamily: 'sans-serif',
                      }}
                    >
                      {orderItem.updatedAt}
                    </Typography> */}
                  </Stack>

                  <Stack direction="row" alignItems="center">
                    <LoadingButton
                      loading={itemIndexLoading.includes(
                        item.tableId +
                          '_' +
                          orderItem.productId +
                          '_' +
                          orderItem.orderItemId
                      )}
                      variant="contained"
                      sx={{
                        minWidth: '48px',
                        borderRadius: '20px',
                        backgroundColor: '#fff',
                        color: 'red',
                        boxShadow: 'none',
                        border: '1px solid red',
                        mr: 1,
                        '&:hover': {
                          backgroundColor: 'red',
                          color: 'white',
                        },
                      }}
                      onClick={() => {
                        handleCookingToStockOutToProduct(
                          item.tableId,
                          item.tableName,
                          orderItem.orderItemId,
                          orderItem.productId,
                          orderItem.productTitle,
                          indexItem
                        )
                      }}
                    >
                      <Iconify
                        icon={'ic:outline-double-arrow'}
                        width={16}
                        sx={{ transform: 'rotate(180deg)' }}
                      />
                    </LoadingButton>
                    <LoadingButton
                      loading={itemIndexLoading.includes(
                        item.tableId +
                          '_' +
                          orderItem.productId +
                          '_' +
                          orderItem.orderItemId
                      )}
                      variant="contained"
                      sx={{
                        minWidth: '48px',
                        borderRadius: '20px',
                        backgroundColor: '#fff',
                        color: 'red',
                        boxShadow: 'none',
                        border: '1px solid red',
                        mr: 3,
                        '&:hover': {
                          backgroundColor: 'red',
                          color: 'white',
                        },
                      }}
                      onClick={() => {
                        handleCookingToStockOutOneProduct(
                          item.tableId,
                          item.tableName,
                          orderItem.orderItemId,
                          orderItem.productId,
                          orderItem.productTitle,
                          indexItem
                        )
                      }}
                    >
                      <Iconify
                        icon={'ic:sharp-keyboard-arrow-left'}
                        width={16}
                      />
                    </LoadingButton>
                    <Typography
                      id="transition-modal-title"
                      variant="body2"
                      sx={{
                        color: 'black',
                        fontSize: '20px',
                        fontWeight: 'bold',
                        fontFamily: 'serif',
                        mr: 3,
                      }}
                    >
                      {orderItem.quantity}
                    </Typography>

                    <LoadingButton
                      // loading={
                      //     loadingOneProductTable &&
                      //     index === indexLoadingOneProductTable &&
                      //     indexTable === indexLoadingAllTable
                      // }
                      // loading={
                      //   kitchenDataStatus === HTTP_STATUS.PENDING &&
                      //   orderItem.orderItemId === indexLoadingOneProduct
                      // }
                      // loading={
                      //   kitchenDataStatus === HTTP_STATUS.PENDING &&
                      //   orderItem.productId + '_' + indexItem === indexLoading
                      // }
                      loading={itemIndexLoading.includes(
                        item.tableId +
                          '_' +
                          orderItem.productId +
                          '_' +
                          orderItem.orderItemId
                      )}
                      variant="contained"
                      sx={{
                        minWidth: '48px',
                        borderRadius: '20px',
                        backgroundColor: '#F9FAFB',
                        color: '#5B509A',
                        boxShadow: 'none',
                        border: '1px solid #5B509A',
                        mr: 1,
                        '&:hover': {
                          backgroundColor: '#5B509A',
                          color: 'white',
                        },
                      }}
                      onClick={() => {
                        changeCookingToWaiterOneProduct(
                          item.tableId,
                          item.tableName,
                          orderItem.orderItemId,
                          orderItem.productId,
                          orderItem.productTitle,
                          indexItem
                        )
                      }}
                    >
                      <Iconify
                        icon={'ic:sharp-keyboard-arrow-right'}
                        width={16}
                      />
                    </LoadingButton>
                    <LoadingButton
                      // loading={
                      //   kitchenDataStatus === HTTP_STATUS.PENDING &&
                      //   orderItem.productId + '_' + indexItem === indexLoading
                      // }
                      loading={itemIndexLoading.includes(
                        item.tableId +
                          '_' +
                          orderItem.productId +
                          '_' +
                          orderItem.orderItemId
                      )}
                      variant="contained"
                      sx={{
                        minWidth: '48px',
                        borderRadius: '20px',
                        backgroundColor: '#5B509A',
                        color: 'white',
                        boxShadow: 'none',
                        border: '1px solid #5B509A',
                        '&:hover': {
                          backgroundColor: '#F9FAFB',
                          color: '#5B509A',
                        },
                      }}
                      onClick={() => {
                        handleCookingToWaiterAllProductOfProductTable(
                          item.tableId,
                          item.tableName,
                          orderItem.orderItemId,
                          orderItem.productId,
                          orderItem.productTitle,
                          indexItem
                        )
                      }}
                    >
                      <Iconify icon={'ic:outline-double-arrow'} width={16} />
                    </LoadingButton>
                  </Stack>
                </Stack>
              </Grid>
            </Box>
          ))}
        </Stack>
      ))}
    </>
  )
}
