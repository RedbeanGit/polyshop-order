package fr.dopolytech.polyshop.order.dtos;

import fr.dopolytech.polyshop.order.dtos.exceptions.DtoException;

interface Dto<T> {
    public void validate() throws DtoException;
    public T toModel() throws DtoException;
    public Dto<T> fromModel(T model) throws DtoException;
}
