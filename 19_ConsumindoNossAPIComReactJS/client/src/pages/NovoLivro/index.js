import React from 'react';
import { Link } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi';

import "./styles.css";

import logoImage from '../../assets/logo.svg'

export default function NovoLivro() {
    return (
        <div className="novo-livro-container">
            <div className="content">
                <section className="form">
                    <img src={logoImage} alt="Logo" />

                    <h1>Adicionar novo livro</h1>

                    <p>Insira as informações do livro e clique em 'Adicionar'!</p>

                    <Link className="back-link" to="/livros">
                        <FiArrowLeft size={16} color="#251FC5" />
                        Home
                    </Link>
                </section>

                <form>
                    <input placeholder="Título" />

                    <input placeholder="Autor" />

                    <input type="date" />
                    
                    <input placeholder="Preço" />

                    <button className="button" type="submit">Adicionar</button>
                </form>
            </div>
        </div>
    );
}