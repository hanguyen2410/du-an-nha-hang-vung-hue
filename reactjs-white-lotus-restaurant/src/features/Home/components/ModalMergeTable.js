import React, { useState } from 'react'
import {
  Card,
  CardContent,
  Typography,
  Paper,
  Fade,
  Box,
  Modal,
} from '@mui/material'

import Iconify from 'components/Iconify'

import { useDispatch, useSelector } from 'react-redux'
import { toast } from 'react-toastify'
import { CombineTable } from '../restaurantSlice'
import { LoadingButton } from '@mui/lab'
import { EnumStatus } from 'constants/EnumStatus'

// ----------------------------------------------------------------------

export default function ModalMergeTable({
  openModalMergeTable,
  currentTable,
  handleCloseModal,
}) {
  const dispatch = useDispatch()

  const [state, setState] = useState({
    currentTableId: -1,
    currentTableName: '',
    targetTableId: -1,
    targetTableName: '',
    active: 0,
    loadingCombine: false,
  })

  const restaurantsData = useSelector((state) => state.baseData.data)
  const tables = restaurantsData.tables

  const busyTable = tables.filter(
    (table) =>
      table.status === EnumStatus.BUSY.status && table.id !== currentTable.id
  )

  const handleSetTable = (tableId, tableName) => {
    setState({
      ...state,
      currentTableId: currentTable.id,
      currentTableName: currentTable.name,
      targetTableId: tableId,
      targetTableName: tableName,
      active: tableId,
    })
  }

  const handleClickCombineTable = (
    currentTableId,
    targetId,
    targetName,
    currentName
  ) => {
    setState({
      ...state,
      loadingCombine: true,
    })

    const obj = {
      currentTableId,
      targetTableId: targetId,
    }

    dispatch(CombineTable(obj))
      .unwrap()
      .then((data) => {
        toast.success(`'${currentName}' và '${targetName}' đã gộp xong.`)
      })
      .catch((error) => {
        const message = error.response.data.message
        toast.error(message)
        // toast.error(`'${currentName}' và '${targetName}' gộp thất bại.`)
      })
      .finally(() => {
        setState({
          ...state,
          active: 0,
          loadingCombine: false,
        })
        handleCloseModal()
      })
  }

  const {
    currentTableId,
    currentTableName,
    targetTableId,
    targetTableName,
    active,
    loadingCombine,
  } = state

  return (
    <Modal
      aria-labelledby="transition-modal-title"
      aria-describedby="transition-modal-description"
      open={openModalMergeTable}
      onClose={handleCloseModal}
      closeAfterTransition
      disableAutoFocus
    >
      <Fade in={openModalMergeTable}>
        <Box
          sx={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            width: 800,
            bgcolor: 'background.paper',
            border: '2px solid #d8d8df',
            borderRadius: '12px',
            boxShadow: 24,
            p: 3,
            textAlign: 'center',
          }}
        >
          <Iconify
            icon={'material-symbols:close'}
            position="absolute"
            top="-10px"
            right="-10px"
            bgcolor="white"
            color="#5A5A72"
            borderRadius="50%"
            border="1px solid #d8d8df"
            width={36}
            padding={0.6}
            cursor="pointer"
            boxShadow="rgba(0, 0, 0, 0.2) 0px 2px 12px 0px"
            sx={{
              '&:hover': {
                color: '#131318',
                bgcolor: '#EBEBEF',
                border: '1px solid #B9B9c6',
              },
            }}
            onClick={handleCloseModal}
          />

          <div className="row">
            <Typography
              id="transition-modal-title"
              variant="h6"
              component="h2"
              sx={{
                color: '#53382c',
                textTransform: 'uppercase',
                fontWeight: 'bolder',
                fontFamily: 'fontkhachhang',
              }}
            >
              VUI LÒNG CHỌN BÀN ĐỂ GHÉP
            </Typography>
          </div>
          <div className="row">
            <div className="col-3 d-flex justify-content-center align-items-center">
              <Card sx={{ width: '100%' }}>
                <CardContent>
                  <Box>
                    <Paper
                      key={currentTable.name}
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
                        <Typography variant="h6">
                          {currentTable.name}
                        </Typography>
                      </Box>
                    </Paper>
                  </Box>
                </CardContent>
              </Card>
            </div>
            <div className="col-1 d-flex justify-content-center align-items-center">
              <Iconify
                icon={'mdi:merge'}
                width={100}
                // sx={{
                //     width: "100px"
                // }}
              />
            </div>
            <div className="col-8">
              <Card>
                <CardContent>
                  <Box
                    sx={{
                      display: 'grid',
                      gap: 4,
                      gridTemplateColumns: 'repeat(4, 1fr)',
                    }}
                  >
                    {busyTable &&
                      busyTable.map((item) => (
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
                          ></Box>

                          <Box
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
                            className={`${
                              active === item.id ? 'active' : ''
                            } "`}
                          >
                            <Typography
                              variant="h6"
                              onClick={() => {
                                handleSetTable(item.id, item.name)
                              }}
                            >
                              {item.name}
                            </Typography>
                          </Box>
                        </Paper>
                      ))}
                  </Box>
                </CardContent>
              </Card>
            </div>
          </div>
          <div className="row d-flex justify-content-center align-items-center">
            <div
              className="row d-flex justify-content-center align-items-center"
              style={{ width: '20%' }}
            >
              <LoadingButton
                loading={loadingCombine}
                sx={{
                  color: 'white',
                  backgroundColor: '#4B4086',
                  border: '1px solid #4B4086',
                  mt: 2,
                  '&:hover': {
                    backgroundColor: 'white',
                    color: '#4B4086',
                  },
                }}
                onClick={() => {
                  handleClickCombineTable(
                    currentTableId,
                    targetTableId,
                    targetTableName,
                    currentTableName
                  )
                }}
              >
                Ghép bàn
              </LoadingButton>
            </div>
          </div>
        </Box>
      </Fade>
    </Modal>
  )
}
