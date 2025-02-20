-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 31-03-2021 a las 11:17:52
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
-- Base de datos: `springjpa`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databasefield`
--

CREATE TABLE `databasefield` (
  `id` bigint(20) NOT NULL,
  `database_table_id` bigint(20) NOT NULL,
  `field_name` varchar(50) NOT NULL,
  `field_type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `databasefield`
--

INSERT INTO `databasefield` (`id`, `database_table_id`, `field_name`, `field_type`) VALUES
(1, 1, 'Id', 'INTEGER'),
(2, 1, 'Nombre', 'VARCHAR'),
(3, 1, 'Apellidos', 'VARCHAR'),
(4, 2, 'Id', 'INTEGER'),
(5, 2, 'Clave_tabla1', 'INTEGER'),
(6, 3, 'Id', 'INTEGER'),
(7, 3, 'Clave_tabla2', 'INTEGER'),
(8, 4, 'id', 'BIGINT'),
(9, 4, 'marca', 'VARCHAR'),
(10, 4, 'unidades', 'INT'),
(11, 5, 'idTipo', 'BIGINT'),
(12, 5, 'id', 'BIGINT'),
(13, 5, 'nombre', 'VARCHAR'),
(14, 5, 'unidades', 'INT');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databaseinfo`
--

CREATE TABLE `databaseinfo` (
  `id` bigint(20) NOT NULL,
  `database_name` varchar(50) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `databaseinfo`
--

INSERT INTO `databaseinfo` (`id`, `database_name`, `user_id`) VALUES
(1, 'accessExample.accdb', 1),
(2, 'ejemplo2.sql', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databaserelation`
--

CREATE TABLE `databaserelation` (
  `id` bigint(20) NOT NULL,
  `database_field_id` bigint(20) NOT NULL,
  `database_referenced_field_id` bigint(20) NOT NULL,
  `database_table_id` bigint(20) NOT NULL,
  `database_referenced_table_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `databaserelation`
--

INSERT INTO `databaserelation` (`id`, `database_field_id`, `database_referenced_field_id`, `database_table_id`, `database_referenced_table_id`) VALUES
(1, 5, 1, 2, 1),
(2, 7, 4, 3, 2),
(3, 11, 8, 5, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databasetable`
--

CREATE TABLE `databasetable` (
  `id` bigint(20) NOT NULL,
  `database_id` bigint(20) NOT NULL,
  `table_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `databasetable`
--

INSERT INTO `databasetable` (`id`, `database_id`, `table_name`) VALUES
(1, 1, 'tabla_ejemplo1'),
(2, 1, 'tabla_ejemplo2'),
(3, 1, 'tabla_ejemplo3'),
(4, 2, 'tabla_ejemplo1'),
(5, 2, 'tabla_ejemplo2');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_MODERATOR'),
(3, 'ROLE_ADMIN');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`) VALUES
(1, 'admin', 'a@a.com', '$2a$10$Qkxcwkoq//qHNNFPNEZtbOobb2WR89gi0Q9kcGYn0HNIaIB5geMRW');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_roles`
--

CREATE TABLE `user_roles` (
  `role_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `user_roles`
--

INSERT INTO `user_roles` (`role_id`, `user_id`) VALUES
(1, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `databasefield`
--
ALTER TABLE `databasefield`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_databasefield_databasetable` (`database_table_id`);

--
-- Indices de la tabla `databaseinfo`
--
ALTER TABLE `databaseinfo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_databaseinfo_users` (`user_id`);

--
-- Indices de la tabla `databaserelation`
--
ALTER TABLE `databaserelation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_databaserelation_databasetable1` (`database_table_id`),
  ADD KEY `FK_databaserelation_databasetable2` (`database_referenced_table_id`),
  ADD KEY `FK_databaserelation_databasefield3` (`database_field_id`),
  ADD KEY `FK_databaserelation_databasefield4` (`database_referenced_field_id`);

--
-- Indices de la tabla `databasetable`
--
ALTER TABLE `databasetable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_databasetable_databaseinfo` (`database_id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `user_roles`
--
ALTER TABLE `user_roles`
  ADD KEY `FK_userroles_roles` (`role_id`),
  ADD KEY `FK_userroles_users` (`user_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `databasefield`
--
ALTER TABLE `databasefield`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `databaseinfo`
--
ALTER TABLE `databaseinfo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `databaserelation`
--
ALTER TABLE `databaserelation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `databasetable`
--
ALTER TABLE `databasetable`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `databasefield`
--
ALTER TABLE `databasefield`
  ADD CONSTRAINT `FK_databasefield_databasetable` FOREIGN KEY (`database_table_id`) REFERENCES `databasetable` (`id`);

--
-- Filtros para la tabla `databaseinfo`
--
ALTER TABLE `databaseinfo`
  ADD CONSTRAINT `FK_databaseinfo_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `databaserelation`
--
ALTER TABLE `databaserelation`
  ADD CONSTRAINT `FK_databaserelation_databasefield3` FOREIGN KEY (`database_field_id`) REFERENCES `databasefield` (`id`),
  ADD CONSTRAINT `FK_databaserelation_databasefield4` FOREIGN KEY (`database_referenced_field_id`) REFERENCES `databasefield` (`id`),
  ADD CONSTRAINT `FK_databaserelation_databasetable1` FOREIGN KEY (`database_table_id`) REFERENCES `databasetable` (`id`),
  ADD CONSTRAINT `FK_databaserelation_databasetable2` FOREIGN KEY (`database_referenced_table_id`) REFERENCES `databasetable` (`id`);

--
-- Filtros para la tabla `databasetable`
--
ALTER TABLE `databasetable`
  ADD CONSTRAINT `FK_databasetable_databaseinfo` FOREIGN KEY (`database_id`) REFERENCES `databaseinfo` (`id`);

--
-- Filtros para la tabla `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FK_userroles_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FK_userroles_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
