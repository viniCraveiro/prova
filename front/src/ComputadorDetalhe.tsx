import Add from '@mui/icons-material/Add';
import Delete from '@mui/icons-material/Delete';
import { Alert, Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, Snackbar, Table, TableBody, TableCell, TableHead, TableRow, TextField } from '@mui/material';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import axios from 'axios';
import { Computador, Periferico } from './types';
interface Props {
  operacao: "editando" | "visualizando" | "criando";
}

const computadorLimpo: Computador = { nome: '', cor: '', dataFabricacao: 0, perifericos: [], id: '' };

const ComputadorDetalhe = ({ operacao }: Props) => {
  const url = `http://localhost:8080/api/computador`;
  const { id } = useParams<{ id: string; }>();
  const navigate = useNavigate();
  const [computador, setComputador] = useState<Computador>(computadorLimpo);
  const [openDialog, setOpenDialog] = useState<boolean>(false);
  const [periferico, setPeriferico] = useState<string>('');
  const [openSnackBar, setOpenSnackBar] = useState<boolean>(false);
  const [msgErro, setMsgErro] = useState<string>('');
  const [submited, setSubmited] = useState<boolean>(false);
  const [submitedDialog, setSubmitedDialog] = useState<boolean>(false);
  const [errors, setErrors] = useState<{ nome: boolean; cor: boolean; dataFabricacao: boolean; }>({
    nome: false,
    cor: false,
    dataFabricacao: false,
  });

  useEffect(() => {
    if (operacao === 'editando' || operacao === 'visualizando' && id) {
      axios.get(`http://localhost:8080/api/computador/${id}`)
        .then(response => setComputador(response.data))
        .catch(error => console.log(error));
    }
  }, [id, operacao]);

  const validateFields = () => {
    const newErrors = {
      nome: !computador.nome,
      cor: !computador.cor,
      dataFabricacao: !computador.dataFabricacao || isNaN(computador.dataFabricacao) || computador.dataFabricacao >= new Date().getFullYear() || computador.dataFabricacao < 0,
    };
    setErrors(newErrors);
    return !newErrors.nome && !newErrors.cor && !newErrors.dataFabricacao;
  };

  const handleSave = () => {
    setSubmited(true);
    if (!validateFields()) {
      setMsgErro('Por favor, preencha todos os campos obrigatórios.');
      setOpenSnackBar(true);
      return;
    }
    const request = operacao === 'editando' ? axios.put : axios.post;

    request(url, computador)
      .then(() => navigate('/'))
      .catch(error => {
        console.log(error);
        const errorMsg = error.response?.data?.message || error.message || "Erro desconhecido";
        setMsgErro(errorMsg);
        setOpenSnackBar(true);
      });
  };

  const handleCancel = () => {
    setComputador(computadorLimpo);
    navigate('/');
  };

  const handleAddPeriferico = () => {
    const newPeriferico: Periferico = { nome: periferico };
    setSubmitedDialog(true);
    if (!newPeriferico.nome) return;
    setComputador({ ...computador, perifericos: [...computador.perifericos, newPeriferico] });
    setPeriferico('');
    setOpenDialog(false);
    setSubmitedDialog(false);
  };

  return (
    <div className="p-4">
      <TextField
        label="Nome"
        value={computador.nome}
        onChange={(e) => setComputador({ ...computador, nome: e.target.value })}
        fullWidth
        autoFocus
        className="mb-4"
        variant='standard'
        error={errors.nome && submited && computador.nome?.length === 0}
        helperText={errors.nome && submited && computador.nome?.length === 0 ? 'O campo Nome é obrigatório' : ''}
        disabled={operacao === 'visualizando'}
      />
      <TextField
        label="Cor"
        value={computador.cor}
        onChange={(e) => setComputador({ ...computador, cor: e.target.value })}
        fullWidth
        className='mb-8'
        variant='standard'
        error={errors.cor && submited && computador.cor?.length === 0}
        helperText={errors.cor && submited && computador.cor?.length === 0 ? 'O campo Cor é obrigatório' : ''}
        disabled={operacao === 'visualizando'}
      />
      <TextField
        label="Ano de Fabricação"
        type="number"
        value={computador.dataFabricacao}
        onChange={(e) => { setComputador({ ...computador, dataFabricacao: parseInt(e.target.value) }); }}
        fullWidth
        InputProps={{ inputProps: { min: 0, max: new Date().getFullYear() } }}
        maxRows={4}
        className="mb-4"
        variant='standard'
        error={errors.dataFabricacao && submited || (computador.dataFabricacao != undefined && (computador.dataFabricacao < 0 || computador.dataFabricacao >= new Date().getFullYear()))}
        helperText={errors.dataFabricacao && submited && !(
          computador.dataFabricacao && (computador.dataFabricacao < 0 || computador.dataFabricacao >= new Date().getFullYear())
        )
          ? ('O campo Ano de Fabricação é obrigatório')
          : (
            computador.dataFabricacao && (computador.dataFabricacao < 0 || computador.dataFabricacao >= new Date().getFullYear())
          ) ? 'Ano de fabricação inválido'
            : ''}
        disabled={operacao === 'visualizando'}
      />

      {operacao !== 'visualizando' ? <Button startIcon={<Add />} onClick={() => setOpenDialog(true)} className="mt-4">
        Adicionar Periférico
      </Button> : null}

      <Table size='small'>
        <TableHead>
          <TableRow>
            <TableCell sx={{ fontWeight: '700' }}>Periféricos</TableCell>
            {operacao !== 'visualizando' ? <TableCell align='right'>Ações</TableCell> : null}
          </TableRow>
        </TableHead>
        <TableBody>
          {computador.perifericos.map((perif, index) => (
            <TableRow key={index + perif.nome}>
              <TableCell>{perif.nome}</TableCell>
              {operacao !== 'visualizando' ? <TableCell align='right'>
                <IconButton onClick={() => setComputador({
                  ...computador,
                  perifericos: computador.perifericos.filter((_p, i) => i !== index)
                })}>
                  <Delete />
                </IconButton>
              </TableCell> : null}
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} fullWidth>
        <DialogTitle>Adicionar Periférico</DialogTitle>
        <DialogContent>
          <TextField
            label="Nome do Periférico"
            value={periferico}
            onChange={(e) => setPeriferico(e.target.value)}
            fullWidth
            variant='standard'
            error={periferico.length === 0 && submitedDialog}
            helperText={periferico.length === 0 && submitedDialog ? 'O campo Nome do Periférico é obrigatório' : ' '}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancelar</Button>
          <Button onClick={handleAddPeriferico}>Adicionar</Button>
        </DialogActions>
      </Dialog>


      <div className='flex justify-between'>
        <Button variant="contained" color="primary" onClick={handleCancel} className="mt-4" size='small'> {operacao === 'criando' ? 'Cancelar' : 'Voltar'}</Button>
        {operacao !== 'visualizando' ? <Button variant="contained" color="primary" onClick={handleSave} className="mt-4">
          {operacao === 'editando' ? 'Salvar' : 'Adicionar'}
        </Button> : null}
      </div>

      <Snackbar
        open={openSnackBar}
        autoHideDuration={1000}
        onClose={() => setOpenSnackBar(false)}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert onClose={() => setOpenSnackBar(false)} severity="error">
          {msgErro}
        </Alert>
      </Snackbar>

    </div>
  );
};

export default ComputadorDetalhe;
