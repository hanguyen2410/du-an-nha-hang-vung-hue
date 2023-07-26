import MenuItem from '@mui/material/MenuItem'

// @mui
import PropTypes from 'prop-types'

import {
  Box,
  Card,
  Paper,
  Typography,
  CardContent,
  Popover,
} from '@mui/material'

import { useDispatch, useSelector } from 'react-redux'

// ----------------------------------------------------------------------
import React, { useState } from 'react'

// SweetAlert
import 'layouts/sweetalert.css'

// components
import Tooltip from '@mui/material/Tooltip'
import Iconify from 'components/Iconify'

// enum status tables
import { EnumStatus } from 'constants/EnumStatus'
import ModalChangeTable from './ModalChangeTable'
import ModalMergeTable from './ModalMergeTable'
import { toast } from 'react-toastify'

import {
  changeTable,
  UnCombineTable,
  setDataLoading,
  closeCurrentTable,
  updateChangeTableStatus,
} from 'features/Home/restaurantSlice'

export default function AppTablesList({
  onClick,
  title,
  subheader,
  closeTable,
  handleSelectTable,
  ...other
}) {
  const dispatch = useDispatch()

  const baseData = useSelector((state) => state.baseData.data)
  const loadingGlobal = baseData.loading

  const restaurantsData = useSelector((state) => state.baseData.data)
  const tables = restaurantsData.tables

  const [openMenuTable, setOpenMenuTable] = useState(null)
  const [checkOpenMenuTable, setCheckOpenMenuTable] = useState(false)
  const [openModalChangeTable, setOpenModalChangeTable] = useState(false)
  const [openModalMergeTable, setOpenModalMergeTable] = useState(false)
  const [currentTable, setCurrentTable] = useState({})
  const [selectTable, setSelectTable] = useState({})

  const emptyTable = tables.filter(
    (table) => table.status === EnumStatus.EMPTY.status
  )

  const handleCloseMenu = () => {
    setOpenMenuTable(null)
    setCheckOpenMenuTable(false)
  }

  const handleOpenMenuTable = (e, table) => {
    setOpenMenuTable(e.currentTarget)
    setCurrentTable(table)
  }

  const handleOpenMenuChangeTable = (e) => {
    setOpenModalChangeTable(true)
    setOpenMenuTable(false)
  }

  const handleCloseModal = (e) => {
    setOpenModalChangeTable(false)
    setOpenModalMergeTable(false)
  }

  const handleOpenModalMergeTable = (e) => {
    setOpenModalMergeTable(true)
    setOpenMenuTable(false)
  }

  const handleCloseTable = () => {
    let obj = {
      currentTableId: currentTable.id,
    }
    dispatch(closeCurrentTable(obj))
      .unwrap()
      .then(() => {
        toast.success(`đóng '${currentTable.name}' thành công.`)
      })
      .catch(() => {
        toast.error(`đóng bàn thất bại.`)
      })
  }

  const handleSelectTableChange = (e, table) => {
    setSelectTable(table)
  }

  const handleChangeTable = () => {
    let obj = {
      oldTableId: currentTable.id,
      newTableId: selectTable.id,
    }

    dispatch(setDataLoading(true))
    dispatch(changeTable(obj))
      .unwrap()
      .then((data) => {
        handleCloseModal()
        const oldTable = {
          tableId: currentTable.id,
          status: EnumStatus.EMPTY.status,
          statusValue: EnumStatus.EMPTY.statusValue,
        }
        const newTable = {
          tableId: selectTable.id,
          status: EnumStatus.BUSY.status,
          statusValue: EnumStatus.BUSY.statusValue,
        }
        dispatch(updateChangeTableStatus(oldTable))
        dispatch(updateChangeTableStatus(newTable))
        dispatch(setDataLoading(false))
        toast.success(
          `Chuyển '${currentTable.name}' sang '${selectTable.name}' thành công.`
        )
      })
      .catch(() => {
        dispatch(setDataLoading(false))
        toast.error(`Thao tác chuyển bàn thất bại.`)
      })
  }

  const handleClickUnCombineTable = (currentTableId, currentTableName) => {
    const obj = {
      currentTableId,
    }

    dispatch(UnCombineTable(obj))
      .unwrap()
      .then((data) => {
        toast.success(`'${currentTableName}' đã tách thành công.`)
      })
      .catch(() => {
        toast.error(`'${currentTableName}' tách thất bại.`)
      })
  }

  return (
    <>
      <Card {...other}>
        <CardContent>
          <Box
            sx={{
              display: 'grid',
              gap: 4,
              gridTemplateColumns: 'repeat(4, 1fr)',
            }}
          >
            {tables &&
              tables.map((item) =>
                item.status === EnumStatus.EMPTY.status ? (
                  <Paper
                    key={item.name}
                    variant="outlined"
                    sx={{
                      py: 2.5,
                      textAlign: 'center',
                      paddingTop: 0,
                      paddingBottom: 0,
                      background: 'white',
                    }}
                  >
                    <Box
                      sx={{
                        background: '#d1e9fc',
                        borderBottom: '1px solid #e5e8eb',
                      }}
                    >
                      <Tooltip title="Mở bàn">
                        <Iconify
                          icon={'mdi:bell-circle'}
                          color="black"
                          width={32}
                          cursor="pointer"
                          onClick={() => {
                            handleSelectTable(item)
                          }}
                          sx={{
                            '&:hover': {
                              color: 'red',
                              opacity: [0.9, 0.8, 0.7],
                            },
                          }}
                        />
                      </Tooltip>
                    </Box>

                    <Box
                      onClick={() => {
                        handleSelectTable(item)
                      }}
                      sx={{
                        cursor: 'pointer',
                        pt: 1.5,
                        pb: 1.5,
                        '&:hover': {
                          backgroundColor: 'orange',
                          color: 'white',
                          opacity: [0.9, 0.8, 0.7],
                        },
                      }}
                    >
                      <Typography variant="body2" sx={{ color: 'gray' }}>
                        {EnumStatus.EMPTY.statusValue}
                      </Typography>

                      <Typography variant="h6">{item.name}</Typography>
                    </Box>
                  </Paper>
                ) : item.status === EnumStatus.BUSY.status ? (
                  <Paper
                    key={item.name}
                    variant="outlined"
                    sx={{
                      py: 2.5,
                      textAlign: 'center',
                      paddingTop: 0,
                      paddingBottom: 0,
                      background: '#7266ba',
                    }}
                  >
                    <Box
                      sx={{
                        background: '#4b4086',
                        borderBottom: '1px solid #e5e8eb',
                      }}
                    >
                      <Iconify
                        icon={'mdi:bell-circle'}
                        color="white"
                        width={32}
                        cursor="pointer"
                        onClick={(e) => handleOpenMenuTable(e, item)}
                        sx={{
                          '&:hover': {
                            color: 'yellow',
                            opacity: [0.9, 0.8, 0.7],
                          },
                        }}
                      />
                    </Box>

                    <Box
                      onClick={() => {
                        handleSelectTable(item)
                      }}
                      sx={{
                        cursor: 'pointer',
                        pt: 1.5,
                        pb: 1.5,
                        '&:hover': {
                          backgroundColor: 'orange',
                          color: 'white',
                          opacity: [0.9, 0.8, 0.7],
                        },
                      }}
                    >
                      <Typography variant="body2" sx={{ color: 'white' }}>
                        {EnumStatus.BUSY.statusValue}
                      </Typography>

                      <Typography variant="h6" sx={{ color: 'white' }}>
                        {item.name}
                      </Typography>
                    </Box>
                  </Paper>
                ) : item.status === EnumStatus.COMBINE.status ? (
                  <Paper
                    key={item.name}
                    variant="outlined"
                    sx={{
                      py: 2.5,
                      textAlign: 'center',
                      paddingTop: 0,
                      paddingBottom: 0,
                      background: '#068DA9',
                    }}
                  >
                    <Box
                      sx={{
                        background: '#526D82',
                        borderBottom: '1px solid #e5e8eb',
                      }}
                    >
                      <Iconify
                        icon={'mdi:bell-circle'}
                        color="white"
                        width={32}
                        cursor="pointer"
                        onClick={(e) => handleOpenMenuTable(e, item)}
                        sx={{
                          '&:hover': {
                            color: 'yellow',
                            opacity: [0.9, 0.8, 0.7],
                          },
                        }}
                      />

                      <Iconify
                        icon={'mdi:undo-variant'}
                        color="white"
                        width={32}
                        cursor="pointer"
                        onClick={() => {
                          handleClickUnCombineTable(item.id, item.name)
                        }}
                        sx={{
                          marginLeft: 3,
                          '&:hover': {
                            color: 'red',
                            opacity: [0.9, 0.8, 0.7],
                          },
                        }}
                      />
                    </Box>

                    <Box
                      onClick={() => {
                        handleSelectTable(item)
                      }}
                      sx={{
                        cursor: 'pointer',
                        pt: 1.5,
                        pb: 1.5,
                        '&:hover': {
                          backgroundColor: 'orange',
                          color: 'white',
                          opacity: [0.9, 0.8, 0.7],
                        },
                      }}
                    >
                      <Typography variant="body2" sx={{ color: 'white' }}>
                        {EnumStatus.COMBINE.statusValue}
                      </Typography>

                      <Typography variant="h6" sx={{ color: 'white' }}>
                        {item.name}
                      </Typography>
                    </Box>
                  </Paper>
                ) : (
                  <Paper
                    key={item.name}
                    variant="outlined"
                    sx={{
                      py: 2.5,
                      textAlign: 'center',
                      paddingTop: 0,
                      paddingBottom: 0,
                      background: '#c2722b',
                    }}
                  >
                    <Box
                      sx={{
                        background: '#9d510e',
                        borderBottom: '1px solid #e5e8eb',
                      }}
                    >
                      <Iconify
                        icon={'mdi:bell-circle'}
                        color="white"
                        width={32}
                        cursor="pointer"
                        // onClick={(e) => {
                        //     handleOpenMenu(e, table);
                        // }}
                        sx={{
                          '&:hover': {
                            color: '#40c940',
                            opacity: [0.9, 0.8, 0.7],
                          },
                        }}
                      />
                    </Box>

                    <Box
                      onClick={() => {
                        onClick(item)
                      }}
                      sx={{
                        cursor: 'pointer',
                        pt: 1.5,
                        pb: 1.5,
                        '&:hover': {
                          backgroundColor: 'orange',
                          color: 'white',
                          opacity: [0.9, 0.8, 0.7],
                        },
                      }}
                    >
                      <Typography variant="body2" sx={{ color: 'white' }}>
                        {EnumStatus.BOOKED.statusValue}
                      </Typography>

                      <Typography variant="h6" sx={{ color: 'white' }}>
                        {item.name}
                      </Typography>
                    </Box>
                  </Paper>
                )
              )}
          </Box>
        </CardContent>
      </Card>
      <Popover
        open={Boolean(openMenuTable)}
        anchorEl={openMenuTable}
        onClose={handleCloseMenu}
        anchorOrigin={{ vertical: 'top', horizontal: 'left' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        PaperProps={{
          sx: {
            p: 1,
            width: 160,
            '& .MuiMenuItem-root': {
              px: 1,
              typography: 'body2',
              borderRadius: 0.75,
            },
          },
        }}
      >
        <MenuItem onClick={handleOpenMenuChangeTable}>
          <Iconify icon={'codicon:arrow-swap'} sx={{ mr: 2 }} />
          Chuyển bàn
        </MenuItem>
        <MenuItem onClick={handleOpenModalMergeTable}>
          <Iconify icon={'codicon:type-hierarchy'} sx={{ mr: 2 }} />
          Ghép bàn
        </MenuItem>
        <MenuItem onClick={handleCloseTable}>
          <Iconify icon={'charm:cross'} sx={{ mr: 2 }} />
          Đóng Bàn
        </MenuItem>
      </Popover>

      <ModalChangeTable
        openModalChangeTable={openModalChangeTable}
        emptyTable={emptyTable}
        currentTable={currentTable}
        handleCloseModal={handleCloseModal}
        handleSelectTableChange={handleSelectTableChange}
        selectTable={selectTable}
        handleChangeTable={handleChangeTable}
      />
      <ModalMergeTable
        openModalMergeTable={openModalMergeTable}
        currentTable={currentTable}
        handleCloseModal={handleCloseModal}
      />
    </>
  )
}
