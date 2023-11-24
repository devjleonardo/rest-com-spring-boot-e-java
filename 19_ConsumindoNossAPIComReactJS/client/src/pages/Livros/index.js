import React from 'react';
import { Link } from 'react-router-dom';
import { FiPower, FiEdit, FiTrash2 } from 'react-icons/fi';

import "./styles.css";

import logoImage from '../../assets/logo.svg'

export default function Livros() {
    return (
        <div className="livro-container">
            <header>
                <img src={logoImage} alt= "Logo" />

                <span>Bem-vindo, <strong>Leandro</strong>! </span>

                <Link className="button" to="/livros/novo">Adicionar novo livro</Link>
            
                <button type="button">
                    <FiPower size={18} color="#251FC5" />
                </button>
            </header>

            <h1>Livros Registrados</h1>

            <ul>
                <li>
                    <strong>Título</strong>
                    <p>Docker Deep Dive</p>

                    <strong>Autor:</strong>
                    <p>Nigel Poulton</p>

                    <strong>Preço:</strong>
                    <p>R$ 47,90</p>

                    <strong>Data de lançamento:</strong>
                    <p>12/07/2017</p>

                    <button type="button">
                        <FiEdit size={20} color="#251FC5" />
                    </button>

                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5" />
                    </button>
                </li>

                <li>
                    <strong>Título</strong>
                    <p>Docker Deep Dive</p>

                    <strong>Autor:</strong>
                    <p>Nigel Poulton</p>

                    <strong>Preço:</strong>
                    <p>R$ 47,90</p>

                    <strong>Data de lançamento:</strong>
                    <p>12/07/2017</p>

                    <button type="button">
                        <FiEdit size={20} color="#251FC5" />
                    </button>

                    <button type="button">
                        <FiTrash2 size={20} color="#251FC5" />
                    </button>
                </li>
            </ul>
        </div>
    );
}