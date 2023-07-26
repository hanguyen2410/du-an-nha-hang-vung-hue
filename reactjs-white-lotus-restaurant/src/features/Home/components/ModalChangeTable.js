import React from 'react'
import {
  Card,
  CardContent,
  Typography,
  Paper,
  Button,
  Fade,
  Box,
  Modal,
} from '@mui/material'

import Iconify from 'components/Iconify'

// ----------------------------------------------------------------------

export default function ModalChangeTable({
  openModalChangeTable,
  emptyTable,
  currentTable,
  selectTable,
  handleCloseModal,
  handleSelectTableChange,
  handleChangeTable,
}) {
  return (
    <Modal
      aria-labelledby="transition-modal-title"
      aria-describedby="transition-modal-description"
      open={openModalChangeTable}
      onClose={handleCloseModal}
      closeAfterTransition
      disableAutoFocus
    >
      <Fade in={openModalChangeTable}>
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
              VUI LÒNG CHỌN BÀN ĐỂ CHUYỂN
            </Typography>
          </div>
          <div className="row">
            <div className="col-3 d-flex justify-content-center align-items-center">
              <Card sx={{ width: '100%', backgroundColor: '#fff' }}>
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
                        background: '#4b4086',
                        color: 'white',
                      }}
                    >
                      <Box
                        sx={{
                          pt: 1.5,
                          pb: 1.5,
                          '&:hover': {
                            backgroundColor: '#4B4086',
                            color: 'white',
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
                icon={'icon-park-outline:right-two'}
                width={100}
                // sx={{
                //     width: "100px"
                // }}
              />
            </div>
            <div className="col-8" style={{ margin: '20px 0' }}>
              <Card>
                <CardContent sx={{ backgroundColor: '#fff' }}>
                  <Box
                    sx={{
                      display: 'grid',
                      gap: 4,
                      gridTemplateColumns: 'repeat(4, 1fr)',
                    }}
                  >
                    {emptyTable &&
                      emptyTable.map((item) =>
                        item.id === selectTable.id ? (
                          <Paper
                            key={item.name}
                            variant="outlined"
                            sx={{
                              py: 2.5,
                              textAlign: 'center',
                              paddingTop: 0,
                              paddingBottom: 0,
                              background: '#4B4086',
                              color: 'white',
                            }}
                            onClick={(e) => handleSelectTableChange(e, item)}
                          >
                            <Box
                              sx={{
                                background: 'orange',
                                borderBottom: '1px solid #e5e8eb',
                                color: 'white',
                                opacity: [0.9, 0.8, 0.7],
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
                            >
                              <Typography variant="h6">{item.name}</Typography>
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
                              background: 'white',
                            }}
                            onClick={(e) => handleSelectTableChange(e, item)}
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
                            >
                              <Typography variant="h6">{item.name}</Typography>
                            </Box>
                          </Paper>
                        )
                      )}
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
              <Button
                variant="contained"
                sx={{
                  color: 'white',
                  backgroundColor: '#4B4086',
                  border: '1px solid #4B4086',
                  mt: 2,
                  '&:hover': {
                    backgroundColor: 'white',
                    color: '#7266ba',
                  },
                }}
                onClick={handleChangeTable}
              >
                Chuyển bàn
              </Button>
            </div>
          </div>
        </Box>
      </Fade>
    </Modal>
  )
}
