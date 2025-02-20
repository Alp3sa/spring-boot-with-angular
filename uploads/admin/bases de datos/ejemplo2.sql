-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 14-10-2020 a las 13:48:54
-- Versión del servidor: 10.4.13-MariaDB
-- Versión de PHP: 7.2.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ejemplo`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tabla_ejemplo1`
--

CREATE TABLE `tabla_ejemplo1` (
  `id` bigint(20) NOT NULL,
  `marca` varchar(50) NOT NULL,
  `unidades` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `tabla_ejemplo1`
--

INSERT INTO `tabla_ejemplo1` (`id`, `marca`, `unidades`) VALUES
(1, 'Una', 10),
(2, 'Dos', 20);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tabla_ejemplo2`
--

CREATE TABLE `tabla_ejemplo2` (
  `idTipo` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `unidades` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `tabla_ejemplo2`
--

INSERT INTO `tabla_ejemplo2` (`idTipo`, `id`, `nombre`, `unidades`) VALUES
(2, 1, 'Accesorio1', 2),
(1, 2, 'Accesorio2', 4);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `tabla_ejemplo1`
--
ALTER TABLE `tabla_ejemplo1`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `tabla_ejemplo2`
--
ALTER TABLE `tabla_ejemplo2`
  ADD PRIMARY KEY (`id`),
  ADD KEY `retriccion1` (`idTipo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `tabla_ejemplo1`
--
ALTER TABLE `tabla_ejemplo1`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `tabla_ejemplo2`
--
ALTER TABLE `tabla_ejemplo2`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `tabla_ejemplo2`
--
ALTER TABLE `tabla_ejemplo2`
  ADD CONSTRAINT `retriccion1` FOREIGN KEY (`idTipo`) REFERENCES `tabla_ejemplo1` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
