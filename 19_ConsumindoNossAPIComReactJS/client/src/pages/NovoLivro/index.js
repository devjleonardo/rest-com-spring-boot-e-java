import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi';

import api from "../../services/api";

import "./styles.css";

import logoImage from '../../assets/logo.svg'

export default function NovoLivro() {
    const [id, setId] = useState(null);
    const [titulo, setTitulo] = useState('');
    const [autor, setAutor] = useState('');
    const [dataLancamento, setDataLancamento] = useState('');
    const [preco, setPreco] = useState('');

    const {livroId} = useParams();
    
    const accessToken = localStorage.getItem("accessToken");

    const navigate = useNavigate();

    async function loadLivro() {
        try {
            const response = await api.get(`api/livros/v1/${livroId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
            let adjustedDate = response.data.dataLancamento.split("T", 10)[0];

            setId(response.data.id);
            setTitulo(response.data.titulo);
            setAutor(response.data.autor);
            setDataLancamento(adjustedDate);
            setPreco(response.data.preco);
        } catch (error) {
            alert("Erro ao recuperar o livro! Tente novamente!");
            navigate("/livros");
        }
    }

    useEffect(() => {
        if (livroId === "0") {
            return;
        } else {
            loadLivro();
        }
    }, [livroId])

    async function salvarOuAtualizar(e) {
        e.preventDefault();

        const data = {
            titulo,
            autor,
            dataLancamento,
            preco
        };

        try {
            if (livroId === "0") {
                await api.post("api/livros/v1", data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });
            } else {
                data.id = id

                await api.put("api/livros/v1", data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });    
            }

            navigate("/livros");
        } catch (error) {
            alert("Erro ao recuperar adicionar o livro ! Tente novamente!");
        }
    }

    return (
        <div className="novo-livro-container">
            <div className="content">
                <section className="form">
                    <img src={logoImage} alt="Logo" />

                    <h1>{livroId === "0" ? "Adicionar novo" : "Atualizar"} livro</h1>

                    <p>{livroId === "0" ? "Insira" : "Atualize"} as informações do livro e clique em {livroId === "0" ? "Adicionar" : "Atualizar"}!</p>

                    <Link className="back-link" to="/livros">
                        <FiArrowLeft size={16} color="#251FC5" />
                        Voltar para os registros de livros
                    </Link>
                </section>

                <form onSubmit={salvarOuAtualizar}>
                    <input
                        placeholder="Título" 
                        value={titulo}
                        onChange={e => setTitulo(e.target.value)}
                    />

                    <input
                        placeholder="Autor" 
                        value={autor}
                        onChange={e => setAutor(e.target.value)}
                    />

                    <input
                        type="date" 
                        value={dataLancamento}
                        onChange={e => setDataLancamento(e.target.value)}
                    />

                    <input
                        placeholder="Preço" 
                        value={preco}
                        onChange={e => setPreco(e.target.value)}
                    />

                    <button className="button" type="submit">{livroId === "0" ? "Adicionar" : "Atualizar"}</button>
                </form>
            </div>
        </div>
    );
}