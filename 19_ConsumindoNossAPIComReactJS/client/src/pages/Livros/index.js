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
            }
        }).then(response => {
            setLivros(response.data._embedded.livroDTOList)
        });
    });
    
    return (
        <div className="livro-container">
            <header>
                <img src={logoImage} alt= "Logo" />

                <span>Bem-vindo, <strong>{nomeDeUsuario.toUpperCase()}</strong>! </span>

                <Link className="button" to="/livros/novo">Adicionar novo livro</Link>
            
                <button type="button">
                    <FiPower size={18} color="#251FC5" />
                </button>
            </header>

            <h1>Livros Registrados</h1>

            <ul>
                {livros.map(livro => (
                    <li>
                        <strong>Título</strong>
                        <p>{livro.titulo}</p>

                        <strong>Autor:</strong>
                        <p>{livro.autor}</p>

                        <strong>Preço:</strong>
                        <p>{Intl.NumberFormat("pt-BR", {style: "currency", currency: "BRL"}).format(livro.preco)}</p>

                        <strong>Data de lançamento:</strong>
                        <p>{Intl.DateTimeFormat("pt-BR").format(livro.dataNascimento)}</p>

                        <button type="button">
                            <FiEdit size={20} color="#251FC5" />
                        </button>

                        <button type="button">
                            <FiTrash2 size={20} color="#251FC5" />
                        </button>
                    </li>
                 ))}
            </ul>
        </div>
    );
}