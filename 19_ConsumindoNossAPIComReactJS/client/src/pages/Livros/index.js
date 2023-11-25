import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiPower, FiEdit, FiTrash2 } from 'react-icons/fi';

import api from "../../services/api";

import "./styles.css";

import logoImage from '../../assets/logo.svg'

export default function Livros() {
    const [livros, setLivros] = useState([]);

    const nomeDeUsuario = localStorage.getItem("nomeDeUsuario");
    const accessToken = localStorage.getItem("accessToken");

    const navigate = useNavigate();

    useEffect(() => {
        api.get("api/livros/v1", {
            headers: {
                Authorization: `Bearer ${accessToken}`
            },
            params: {
                page: 1,
                size: 4,
                direction: "asc"
            }
        }).then(response => {
            setLivros(response.data._embedded.livroDTOList)
        });
    });

    async function editarLivro(id) {
        try {
            navigate(`/livros/novo/${id}`)
        } catch (error) {
            alert("Falha ao editar! Tente novamente.");
        }
    }

    async function deletarLivro(id) {
        try {
            await api.delete(`api/livros/v1/${id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })

            setLivros(livros.filter(livro => livro.id !== id))
        } catch (error) {
            alert("Falha ao deletar! Tente novamente.");
        }
    }

    async function logout() {
        localStorage.clear();

        navigate("/");
    }
    
    return (
        <div className="livro-container">
            <header>
                <img src={logoImage} alt= "Logo" />

                <span>Bem-vindo, <strong>{nomeDeUsuario.toUpperCase()}</strong>! </span>

                <Link className="button" to="/livros/novo/0">Adicionar novo livro</Link>
            
                <button onClick={logout} type="button">
                    <FiPower size={18} color="#251FC5" />
                </button>
            </header>

            <h1>Livros Registrados</h1>

            <ul>
                {livros.map(livro => (
                    <li key={livro.id}>
                        <strong>Título</strong>
                        <p>{livro.titulo}</p>

                        <strong>Autor:</strong>
                        <p>{livro.autor}</p>

                        <strong>Preço:</strong>
                        <p>{Intl.NumberFormat("pt-BR", {style: "currency", currency: "BRL"}).format(livro.preco)}</p>

                        <strong>Data de lançamento:</strong>
                        <p>{Intl.DateTimeFormat("pt-BR").format(livro.dataNascimento)}</p>

                        <button onClick={() => editarLivro(livro.id)} type="button">
                            <FiEdit size={20} color="#251FC5" />
                        </button>

                        <button onClick={() => deletarLivro(livro.id)} type="button">
                            <FiTrash2 size={20} color="#251FC5" />
                        </button>
                    </li>
                 ))}
            </ul>
        </div>
    );
}