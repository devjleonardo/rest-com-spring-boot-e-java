import React from 'react';
import { Link } from 'react-router-dom';
import { FiPower } from 'react-icons/fi';

import "./styles.css";

import logoImage from '../../assets/logo.svg'

export default function Livro() {
    return (
        <div className="livro-container">
            <header>
                <img src={logoImage} alt= "Logo" />

                <span>Bem-vindo, <strong>Leandro</strong>! </span>

                <Link className="button" to="livros/novo">Adicionar novo livro</Link>
            
                <button type="button">
                    <FiPower size={18} color="#251FC5" />
                </button>
            </header>
        </div>
    );
}