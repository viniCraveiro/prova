import { Add, Delete, Edit, Visibility } from '@mui/icons-material';
import { Button, Container, IconButton, Table, TableBody, TableCell, TableHead, TableRow, TextField, Tooltip } from '@mui/material';
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Computador } from './types';

const App = () => {
    const [computadores, setComputadores] = useState<Computador[]>([]);
    const [search, setSearch] = useState<string>('');
    const navigate = useNavigate();

    useEffect(() => {
        axios.get('http://localhost:8080/api/computador')
            .then(response => setComputadores(response.data))
            .catch(error => console.log(error));
    }, []);

    const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearch(e.target.value);
    };

    const handleDelete = (id: string) => {
        axios.delete(`http://localhost:8080/api/computador/${id}`)
            .then(() => setComputadores(computadores.filter(comp => comp.id !== id)))
            .catch(error => console.log(error));
    };

    return (
        <Container className="mt-4">
            <div className="flex  mb-4">
                <TextField
                    variant="standard"
                    label="Pesquisar por nome"
                    value={search}
                    onChange={handleSearch}
                    sx={{ width: '50%', paddingRight: '2rem' }}
                    size='small'
                />
                <Button
                    variant="contained"
                    startIcon={<Add />}
                    onClick={() => navigate('/novo')}
                    size="small"
                >Novo
                </Button>
            </div>

            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell width={'40%'}>Nome</TableCell>
                        <TableCell>Cor</TableCell>
                        <TableCell>Ano Fabricação</TableCell>
                        <TableCell align='right'>Ações</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {computadores.filter(c => c.nome?.toLowerCase().includes(search.toLowerCase()))
                        .map((computador) => (
                            <TableRow key={computador.id}>
                                <TableCell>
                                    <Tooltip title={computador.perifericos.map(perif => <div key={perif.nome}>{perif.nome}</div>)}>
                                        <span>{computador.nome}</span>
                                    </Tooltip>
                                </TableCell>
                                <TableCell>{computador.cor}</TableCell>
                                <TableCell>{computador.dataFabricacao}</TableCell>
                                <TableCell align='right'>
                                    <IconButton onClick={() => navigate(`/visualizar/${computador.id}`)}>
                                        <Visibility />
                                    </IconButton>
                                    <IconButton onClick={() => navigate(`/editar/${computador.id}`)}>
                                        <Edit />
                                    </IconButton>
                                    <IconButton onClick={() => handleDelete(computador.id)}>
                                        <Delete />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                </TableBody>
            </Table>
        </Container>
    );
};

export default App;
