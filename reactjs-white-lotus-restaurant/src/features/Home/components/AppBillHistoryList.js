import { useState, useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { Stack, Tooltip, tooltipClasses, Button } from '@mui/material'
import { styled } from '@mui/material/styles'

import Helper from 'utils/Helper'
import { BillStatus } from 'constants/BillStatus'

import {
  setDataLoading,
  getBillsByDay,
  updateListBillShow,
  getOrderItemsByOrderId,
  updateCurrentBill,
  updateCurrenrOrderitemsBill,
} from 'features/Home/restaurantSlice'

import Iconify from 'components/Iconify'
import { toast } from 'react-toastify'
import ModalDetailBill from './ModalDetailBill'

const LightTooltip = styled(({ className, ...props }) => (
  <Tooltip {...props} classes={{ popper: className }} />
))(({ theme }) => ({
  [`& .${tooltipClasses.tooltip}`]: {
    backgroundColor: theme.palette.common.white,
    color: 'rgba(0, 0, 0, 0.87)',
    boxShadow: theme.shadows[1],
    fontSize: 14,
  },
}))
// ----------------------------------------------------------------------
export default function AppBillHistoryList({
  handleOpenModalOrder,
  checkSearchEmpty,
  ...other
}) {
  const [state, setState] = useState({
    currentDay: '',
    previousDay: '',
  })

  const [openModalBillHistory, setOpenModalBillHistory] = useState(false)

  const getDay = () => {
    let current = new Date()
    let previous = new Date(current.getTime())
    previous.setDate(current.getDate() - 1)
    setState({
      ...state,
      currentDay: Helper.formatDate(current),
      previousDay: Helper.formatDate(previous),
    })
  }

  useEffect(() => {
    try {
      getDay()
    } catch (error) {
      setState({
        ...state,
        erroMessage: error.message,
      })
    }
  }, [])

  const dispatch = useDispatch()

  const baseData = useSelector((state) => state.baseData.data)

  const listBillToday = baseData.listBillToday
  const listBillPreviousDay = baseData.listBillPreviousDay
  const listBillShow = baseData.listBillShow
  const currentBill = baseData.currentBill

  const handleGetBillByDay = () => {
    dispatch(setDataLoading(true))
    dispatch(getBillsByDay())
      .unwrap()
      .then((data) => {
        document.getElementById('bill-value').value = 1
      })
      .catch(() => {})
      .finally(() => {
        dispatch(setDataLoading(false))
      })
  }

  const handleChangeBillHistoryDay = (e) => {
    let billDayValue = +e.target.value
    if (billDayValue === 1) {
      dispatch(updateListBillShow(listBillToday))
    } else {
      dispatch(updateListBillShow(listBillPreviousDay))
    }
  }

  const handleViewDetailBill = (bill) => {
    dispatch(getOrderItemsByOrderId(bill.orderId))
      .unwrap()
      .then((orderItems) => {
        dispatch(updateCurrenrOrderitemsBill(orderItems))
        setOpenModalBillHistory(true)
        dispatch(updateCurrentBill(bill))
      })
      .catch(() => {
        toast.error('Lỗi hệ thống, không thể xem bill')
      })
      .finally(() => {
        dispatch(setDataLoading(false))
      })
  }

  const closeModalBillHistory = () => {
    setOpenModalBillHistory(false)
  }

  const { currentDay, previousDay } = state

  return (
    <>
      {openModalBillHistory && currentBill && (
        <ModalDetailBill
          openModalBillHistory={openModalBillHistory}
          closeModalBillHistory={closeModalBillHistory}
        />
      )}
      <Stack
        direction="row"
        alignItems="center"
        justifyContent="flex-end"
        sx={{
          width: '100%',
          height: 40,
          marginBottom: '5px',
        }}
      >
        <div className="col-12 d-flex ms-auto">
          <div className="ms-2 me-2 d-flex justify-content-center align-items-center">
            Danh sách hóa đơn ngày
          </div>
          <div className="me-2">
            <select
              className="form-select select2-container"
              id="bill-value"
              onChange={handleChangeBillHistoryDay}
            >
              <option value="1">{currentDay}</option>
              <option value="2">{previousDay}</option>
            </select>
          </div>
          <div className="me-2">
            <Button
              sx={{
                color: 'white',
                backgroundColor: '#7266ba',
                width: 'auto',
                height: '37.5px',
                padding: '6px 12px',
                fontSize: '16px',
                '&:hover': {
                  color: '#white',
                  backgroundColor: '#5b509a',
                },
              }}
              onClick={() => {
                handleGetBillByDay()
              }}
            >
              Lấy hoá đơn
            </Button>
          </div>
        </div>
      </Stack>
      <Stack
        direction="row"
        alignItems="center"
        justifyContent="flex-end"
        sx={{
          width: '100%',
          // height: 40,
        }}
      >
        <div className="col-sm-12">
          <div className="card card-table">
            <div className="card-body">
              <div className="col-12"></div>
              <div className="table-responsive noSwipe col-12">
                <table id="tbBill" className="table table-striped table-hover">
                  <thead>
                    <tr>
                      <th className="text-center">#</th>
                      <th className="text-center">Bàn</th>
                      <th className="text-center">Thời gian</th>
                      <th className="text-center">Tên nhân viên</th>
                      <th className="text-center">Tổng tiền</th>
                      <th className="text-center">Trạng thái</th>
                      <th className="text-center"></th>
                    </tr>
                  </thead>
                  <tbody>
                    {listBillShow &&
                      listBillShow.map((item) => (
                        <tr key={item.id}>
                          <td>{item.id}</td>
                          <td>{item.tableName}</td>
                          <td>{item.createdAt}</td>
                          <td>{item.staffName}</td>
                          <td className="text-end">
                            {Helper.formatCurrencyToVND(item.totalAmount)}
                          </td>
                          <td>
                            {item.status === BillStatus.ORDERING.status
                              ? BillStatus.ORDERING.statusValue
                              : item.status === BillStatus.TEMPORARY.status
                              ? BillStatus.TEMPORARY.statusValue
                              : item.status === BillStatus.CANCEL.status
                              ? BillStatus.CANCEL.statusValue
                              : BillStatus.PAID.statusValue}
                          </td>
                          <td>
                            <Iconify
                              icon="mdi:eye"
                              sx={{
                                color: '#7266ba',
                                cursor: 'pointer',
                                '&:hover': {
                                  color: '#5B509A',
                                },
                              }}
                              onClick={() => {
                                handleViewDetailBill(item)
                              }}
                            />
                          </td>
                        </tr>
                      ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </Stack>
    </>
  )
}
