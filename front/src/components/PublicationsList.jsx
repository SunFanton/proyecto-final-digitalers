import React from 'react';
import { Component } from 'react';
import Publication from './Publication';

export default class PublicationsList extends Component{

    constructor(props) {
        super(props);
        this.state = {
            publications: []
        }
    }

    componentDidMount() {
        this.getPublications();
    }

    getPublications = () => {
        var userId = localStorage.getItem("userId");
        const url = "http://localhost:8080/publications/findByUserId/" + userId;
        const header = {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }

        fetch(url, header)
            .then(response => {
                if (!response.ok) throw Error(response.status);
                return response.json();
            }
            )
            .then(json => this.setState({ publications: json }))
            .catch(error => {
                console.error(error);
                localStorage.clear();
                alert("Error al obtener las publicaciones. Intenta ingresar de nuevo");
                window.location.href="/";
            })
            .finally(() => console.info(this.state.publications));
    }


    render(){
        return(
            <>
                <ul>
                {
                    this.state.publications.map(
                        item => (
                            <Publication 
                                key={item.id}
                                id={item.id}
                                title={item.title}
                                body={item.body}
                            />
                        )
                    )
                }
                </ul>
            </>
        )
    }
}